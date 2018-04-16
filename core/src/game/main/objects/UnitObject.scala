package game.main.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.{Interpolation, Vector2}
import game.GameElement
import game.main.MainGame
import game.main.physics.{ObjectType, PhysicsWorld}
import game.main.physics.collision.{CollisionBody, PolygonBody}
import game.main.units.BasicBullet
import game.main.players.Player
import game.main.gameMap.Path
import game.util.Vector2e._
import game.util.{Utils, Vector2e, Vector2mtv}
import sun.font.PhysicalFont

import scala.collection.mutable

/**
  * Created by Frans on 26/02/2018.
  */
class UnitObject(override var sprite: Sprite, var owner: Player,
                 override val physWorld: PhysicsWorld, override val collBody: CollisionBody,
                 override val pos: Vector2, override val size: Vector2) extends ObjectType {

  override val origin: Vector2 = Vector2e(size.x / 2f, size.y / 2f)
  override var mass: Float = 100f
  override var friction: Float = 0.25f

  //movements stats
  val movingForce = Vector2e(0f, 0f)
  val maxForwardForce: Float = 0.04f //max speed = (force * totalFric) / (totalFric - 1)
  val maxAccelerateForce: Float = 0.01f
  val maxSeeAhead: Float = sWidth * 2f

  val maxForceAvoid: Float = 0.025f
  val maxRotateTime: Float = 150f

  val steeringPath: UnitPath = new UnitPath(physWorld.map.path.head, Vector2e(0, 0), 10)

  //units stats
  var health: Float = 100f
  var damage: Float = 10f

  //attack vision
  val visionMaxHeight: Float = size.height * 2f
  val visionMaxDist: Float = size.width * 4f
  val attackVision: CollisionBody = PolygonBody.trapezoidCollBody(size.height,
    visionMaxHeight, visionMaxDist)


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

      if (pos.x > 1920 / 2f - 150) destroy() //TODO because of debug

      updatePhysics()
      updateCollPolygon(attackVision) //update the vision collBox
      updateSprite()
    }
  }

  /** Updates the shooting AI of the unit. */
  private def updateShooting(): Unit = {

    val enemy = physWorld.collideCollisionBody(this, attackVision, null, owner.enemiesAsGameElement)

    enemy.foreach(enemy => {
      val blockingWall = physWorld.collideLine(this, this.pos, enemy.pos, physWorld.mapFilter)

      if (blockingWall.isEmpty) shoot()
    })

  }


  private def selectSteeringTarget(): Vector2 = {

    if (false && owner.colorIndex == 0) mousePos
    else steeringPath.updateTarget(pos)
  }

  /** Updates the movement of the object. */
  private def updateSteering(): Unit = {


    val target = Vector2e.pool(selectSteeringTarget())

    //move towards target
    val steering =
      (((Vector2e.pool(target) -- pos).nor ** maxForwardForce) -- movingForce)
        .limit(maxAccelerateForce) / mass

    //if no obstacle at close distance found, try look further away
    val avoid = Vector2e.pool()
    val obstacleFound = avoidObstacles(0, avoid)


    //if enough speed, try to look further away
    if (!obstacleFound && movingForce.len2() > maxAccelerateForce / 100f) {
      avoidObstacles(maxSeeAhead, avoid)
    }

    //updates steering to avoid obstacles
    steering ++ avoid

    //adds limited movingVelocity to real velocity
    movingForce.mulAdd(steering, ticker.delta).limit(maxForwardForce)
    velocity.add(movingForce)

    //rotates smoothly towards moving direction
    angle = Utils.closestAngle360(angle, movingForce.angle)
    angle = Interpolation.linear.apply(angle, movingForce.angle, ticker.delta / maxRotateTime)
    angle = Utils.absAngle(angle)

    //free the memory
    Vector2e.free(steering)
    Vector2e.free(avoid)
    Vector2e.free(target)

  }


  /** Check if obstacles are ahead. Sets the 'avoid' Vector2 to
    * push the player around the obstacle
    *
    * @param visionLength distance to look ahead
    * @param avoid        a Vector2 that is set to push the player away from the obstacle
    * @return true if there were an obstacle
    */
  private def avoidObstacles(visionLength: Float, avoid: Vector2): Boolean = {

    val ahead = Vector2e.pool(movingForce).nor **
      (visionLength * (movingForce.len() / maxForwardForce)) ++ pos

    //checks collision in the wanted pos
    val obstacle =
      physWorld.collideCircle(this, ahead, collBody.getRadiusScaled, collFilter)

    //draws debug circle
    //if(MainGame.drawCollBox) MainGame.debugRender.circle(ahead.x, ahead.y, collBody.getRadiusScaled)

    //calculate the force opposite to obstacle center
    obstacle.foreach(o => ((avoid ++ pos) -- o.collBody.center).nor ** maxForceAvoid / mass)
    Vector2e.free(ahead) //free the memory

    obstacle.isDefined //returns true if collided
  }

  override def draw(shapeRender: ShapeRenderer): Unit = {
    if (MainGame.drawCollBox) {
      collBody.draw(shapeRender)
      attackVision.draw(shapeRender)
    }
  }

  override def draw(batch: Batch): Unit = {
    if (visible) {
      sprite.draw(batch)
    }
  }


  /** Shoots a bullet */
  def shoot(): Unit = {

    //calculates the pos of the bullet and create it
    val bulletPos = Vector2e(movingForce).nor ** (sWidth / 2f + BasicBullet.radius) ++ pos
    val bullet = BasicBullet.create(this, physWorld,
      bulletPos, Vector2e(movingForce).nor ** (maxSpeed * 5),
      owner.colorIndex)

    //sets the bullet statistics
    bullet.damage = damage
    bullet.collFilter ++= owner.enemies.asInstanceOf[mutable.Buffer[GameElement]]
    bullet.collFilter += physWorld.map

    //owner.enemies.asInstanceOf[mutable.Buffer[GameElement]]
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

