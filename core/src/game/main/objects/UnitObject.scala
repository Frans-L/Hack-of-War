package game.main.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.{Interpolation, MathUtils, Vector2}
import com.badlogic.gdx.utils.{Pool, Pools}
import game.GameElement
import game.loader.{GameTextures, UnitTextures}
import game.main.MainGame
import game.main.physics.{ObjectType, PhysicsWorld}
import game.main.physics.collision.{CollisionBody, PolygonBody}
import game.main.units.{BasicBullet, BulletCreator}
import game.main.players.Player
import game.main.gameMap.Path
import game.util.Vector2e._
import game.util.{pools, _}
import scala.collection.mutable

object UnitObject {
  val pool: Pool[UnitObject] = Pools.get(classOf[UnitObject])
}

/**
  * Created by Frans on 26/02/2018.
  */
class UnitObject() extends ObjectType {

  //general
  var owner: Player = _
  override var physWorld: PhysicsWorld = _
  override var collBody: CollisionBody = _

  override var sprite: Sprite = _

  //steering stats
  var steeringPath: Option[UnitPath] = None
  private val movingForce = Vector2e(0f, 0f)
  var maxForwardForce: Float = _
  var maxAccelerateForce: Float = _
  var maxSeeAhead: Float = _
  var maxForceAvoid: Float = _
  var maxRotateTime: Float = _

  //basic stats & shooting
  var health: Float = _
  var damage: Float = _
  var reloadTime: Int = _
  private var reloadTimer: Int = _
  var bulletCreator: BulletCreator = _

  //attack vision
  var visionMaxHeight: Float = _
  var visionMaxDist: Float = _
  var attackVision: CollisionBody = _
  var attackTarget: Option[UnitObject] = None
  var maxForwardForceAttack: Float = _ //speed when attacking


  def this(textures: UnitTextures, size: Vector2, owner: Player,
           physWorld: PhysicsWorld, collBody: CollisionBody) {
    this()
    init(textures, size, owner, physWorld, collBody) // sets all variables a value
  }

  private def mousePos: Vector2 = MainGame.debugViewPort.unproject(Vector2e(Gdx.input.getX, Gdx.input.getY))


  /** Initializes the object. Have to called if obtained from the pool! */
  def init(textures: UnitTextures, size: Vector2, owner: Player,
           physWorld: PhysicsWorld, collBody: CollisionBody): UnitObject.this.type = {

    setToDefault(textures, size, owner, physWorld, collBody)

    updateCollPolygon(collBody) //updates collisionbox
    updateCollPolygon(attackVision)
    updateSprite() //updates sprites
    physWorld.addUnit(owner.asInstanceOf[GameElement], this) //adds update calls

    this
  }

  /** Initializes the all variables of the object.
    * Makes pooling possible. */
  def setToDefault(textures: UnitTextures, size: Vector2, owner: Player,
                   physWorld: PhysicsWorld, collBody: CollisionBody): Unit = {

    this.enabled = true
    this.visible = true
    this.canBeDeleted = false

    this.owner = owner
    this.physWorld = physWorld
    this.collBody = collBody

    this.sprite = if (textures != null) this.createSprite(textures, owner.colorIndex).get else null
    this.shadow = if (textures != null) this.createShadow(textures) else None

    if (size != null) this.size.set(size) else this.size.set(0, 0)
    this.pos.set(0, 0)
    this.angle = 0
    this.origin.set(this.size.x / 2f, this.size.y / 2f)

    //default values
    this.mass = 100f
    this.friction = 0.025f
    this.velocity.set(0, 0)
    this.steeringPath = None
    this.movingForce.set(0, 0)
    this.maxForwardForce = 0.033f //max speed = (force * totalFric) / (totalFric - 1)
    this.maxAccelerateForce = 0.007f

    this.maxSeeAhead = sWidth * 2f
    this.maxForceAvoid = 0.022f
    this.maxRotateTime = 150f

    this.health = 100f
    this.damage = 30f
    this.reloadTime = 150
    this.reloadTimer = 0
    this.bulletCreator = BasicBullet

    this.visionMaxHeight = this.size.height * 3.5f
    this.visionMaxDist = this.size.width * 4f
    this.attackVision = PolygonBody.trapezoidCollBody(this.size.height, visionMaxHeight, visionMaxDist)

    this.attackTarget = None
    this.maxForwardForceAttack = maxForwardForce * 0.5f //speed when attacking
  }

  /**
    * Updates the object collision, physics, AI etc...
    */
  override def update(): Unit = {
    if (enabled) {

      updateSteering()
      updateShooting()

      //if (pos.x > 1920 / 2f - 150) destroy() //TODO because of debug

      updatePhysics()
      updateCollPolygon(attackVision) //update the vision collBox
      updateSprite()

    }
  }

  /** Updates the shape info. */
  override def updateShape(): Unit = {
    super.updateShape()
    updateCollPolygon(attackVision)
  }

  /** Updates the shooting AI of the unit. */
  private def updateShooting(): Unit = {

    if (attackTarget.isEmpty) {
      val enemy = physWorld.collideCollisionBody(this, attackVision, null, owner.enemiesAsGameElement)
      enemy.foreach {
        case enemy: UnitObject => attackTarget = Some(enemy)
        case _ => Unit
      }
    }

    attackTarget.foreach(target => {
      if (!target.canBeDeleted) {
        val blockingWall = physWorld.collideLine(this, this.pos, target.pos, physWorld.mapFilter)
        if (blockingWall.isDefined) attackTarget = None
        else shoot()
      } else {
        attackTarget = None
      }

    })

    //update reloading
    reloadTimer = math.min(reloadTimer + ticker.delta, reloadTime)

  }


  private def selectSteeringTarget(): Vector2 = {

    if (steeringPath.isEmpty || !steeringPath.get.hasNext) mousePos // owner.colorIndex == 3
    else if (attackTarget.isDefined) attackTarget.get.pos
    else steeringPath.get.updateTarget(pos)
  }

  /** Updates the movement of the object. */
  private def updateSteering(): Unit = {


    val target = pools.VectorPool.obtain(selectSteeringTarget())

    //move towards target
    val steering =
      (((pools.VectorPool.obtain(target) -- pos).nor ** maxForwardForce) -- movingForce)
        .limit(maxAccelerateForce) / mass

    //if no obstacle at close distance found, try look further away
    val avoid = pools.VectorPool.obtain()
    val obstacleFound = avoidObstacles(0, avoid)


    //if enough speed, try to look further away
    if (!obstacleFound && movingForce.len2() > maxAccelerateForce / 100f) {
      avoidObstacles(maxSeeAhead, avoid)
    }

    //updates steering to avoid obstacles
    steering ++ avoid

    //adds limited movingVelocity to real velocity
    val limit = if (attackTarget.isDefined) maxForwardForceAttack else maxForwardForce
    movingForce.mulAdd(steering, ticker.delta).limit(limit)
    velocity.add(movingForce)

    //rotates smoothly towards moving direction
    angle = Utils.closestAngle360(angle, movingForce.angle)
    angle = Interpolation.linear.apply(angle, movingForce.angle, ticker.delta / maxRotateTime)
    angle = Utils.absAngle(angle)

    //free the memory
    pools.VectorPool.free(steering)
    pools.VectorPool.free(avoid)
    pools.VectorPool.free(target)

  }


  /** Check if obstacles are ahead. Sets the 'avoid' Vector2 to
    * push the player around the obstacle
    *
    * @param visionLength distance to look ahead
    * @param avoid        a Vector2 that is set to push the player away from the obstacle
    * @return true if there were an obstacle
    */
  private def avoidObstacles(visionLength: Float, avoid: Vector2): Boolean = {

    val ahead = pools.VectorPool.obtain(movingForce).nor **
      (visionLength * (movingForce.len() / maxForwardForce)) ++ pos

    //checks collision in the wanted pos
    val obstacle =
      physWorld.collideCircle(this, ahead, collBody.getRadiusScaled, collFilter)

    //draws debug circle
    if (MainGame.drawCollBox) MainGame.debugRender.circle(ahead.x, ahead.y, collBody.getRadiusScaled)

    //calculate the force opposite to obstacle center
    obstacle.foreach(o => ((avoid ++ pos) -- o.collBody.center).nor ** maxForceAvoid / mass)
    pools.VectorPool.free(ahead) //free the memory

    obstacle.isDefined //returns true if collided
  }

  override def draw(shapeRender: ShapeRenderer): Unit = {
    super.draw(shapeRender)
    if (MainGame.drawCollBox) attackVision.draw(shapeRender)
  }


  /** Shoots a bullet */
  def shoot(): Unit = {
    if (reloadTimer >= reloadTime) {

      //calculates the pos of the bullet and create it
      val bulletPos = Vector2e(movingForce).nor ** (sWidth / 2f + bulletCreator.radius) ++ pos
      val bullet = bulletCreator.create(this, physWorld,
        bulletPos, Vector2e(movingForce).nor ** (maxSpeed * 5),
        owner.colorIndex)

      //sets the bullet statistics
      bullet.damage = damage
      bullet.collFilter ++= owner.enemies.asInstanceOf[mutable.Buffer[GameElement]]
      bullet.collFilter += physWorld.map

      //reset timer
      reloadTimer = 0
    }
  }

  /** Reduces the health of the object and checks if object has diead */
  def reduceHealth(damage: Float): Unit = {
    health -= damage

    if (health <= 0) destroy()
  }

  /** Returns the max speed of the object */
  def maxSpeed: Float =
    maxForwardForce * (friction + physWorld.globalFriction) /
      (friction + physWorld.globalFriction - 1)

  /** Resets the object */
  override def reset(): Unit = {
    Gdx.app.log("unitobject", "reset")
    setToDefault(null, null, null, null, null)
    canBeDeleted = true
  }

  /** Frees the object back to pool. */
  override def delete(): Unit = {
    UnitObject.pool.free(this)
  }

}

