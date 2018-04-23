package game.main.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.{Interpolation, MathUtils, Vector2}
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
import sun.font.PhysicalFont

import scala.collection.mutable

/**
  * Created by Frans on 26/02/2018.
  */
class UnitObject(textures: UnitTextures, override val size: Vector2,
                 var owner: Player,
                 override val physWorld: PhysicsWorld,
                 override val collBody: CollisionBody,
                 var steeringPath: Option[UnitPath]) extends ObjectType {

  //textures
  override var sprite: Sprite = this.createSprite(textures, owner.colorIndex).get
  override protected val shadow: Option[Sprite] = this.createShadow(textures)

  //mandatory variables to object with physics
  override val pos: Vector2 = Vector2e(0, 0)
  override val origin: Vector2 = Vector2e(size.x / 2f, size.y / 2f)
  override var mass: Float = 100f
  override var friction: Float = 0.25f

  //movements stats
  val movingForce = Vector2e(0f, 0f)
  var maxForwardForce: Float = 0.033f //max speed = (force * totalFric) / (totalFric - 1)
  var maxAccelerateForce: Float = 0.007f
  var maxSeeAhead: Float = sWidth * 2f

  var maxForceAvoid: Float = 0.022f
  var maxRotateTime: Float = 150f

  //basic stats
  var health: Float = 100f

  //shooting
  var damage: Float = 30f
  var reloadTime: Int = 150
  private var reloadTimer: Int = 0
  var bulletCreator: BulletCreator = BasicBullet

  //attack vision
  var visionMaxHeight: Float = size.height * 3.5f
  var visionMaxDist: Float = size.width * 4f
  val attackVision: CollisionBody = PolygonBody.trapezoidCollBody(size.height,
    visionMaxHeight, visionMaxDist)

  var attackTarget: Option[UnitObject] = None
  var maxForwardForceAttack: Float = maxForwardForce * 0.5f //speed when attacking


  updateCollPolygon(collBody) //updates collisionbox
  updateCollPolygon(attackVision)
  updateSprite() //updates sprite

  physWorld.addUnit(owner.asInstanceOf[GameElement], this) //adds update calls

  private def mousePos: Vector2 = MainGame.debugViewPort.unproject(Vector2e(Gdx.input.getX, Gdx.input.getY))


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
      if (!target.deleted) {
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

}

