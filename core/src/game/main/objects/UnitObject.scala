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
import game.util.Vector2e._
import game.util.{Utils, Vector2e, Vector2mtv}
import sun.font.PhysicalFont

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

  //units stats
  var health: Float = 100f
  var damage: Float = 10f

  updateCollPolygon() //updates collisionbox
  updateSprite() //updates sprite

  physWorld.addUnit(owner.asInstanceOf[GameElement], this) //adds update calls

  private def target: Vector2 = MainGame.debugViewPort.unproject(Vector2e.pool(Gdx.input.getX, Gdx.input.getY))


  /**
    * Updates the object collision, physics, AI etc...
    */
  override def update(): Unit = {
    if (enabled) {

      updateSteering()
      updateShooting()

      if (pos.x > 1920 / 2f - 150) destroy() //TODO because of debug

      updatePhysics()
      updateSprite()
    }
  }

  /** Updates the shooting AI of the unit. */
  private def updateShooting(): Unit = {
    if (ticker.interval2) {
      shoot()
    }
  }

  /** Updates the movement of the object. */
  private def updateSteering(): Unit = {

    //move towards target
    val steering =
      (((target -- pos).nor ** maxForwardForce) -- movingForce)
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
    val visionPos = Vector2e.pool(ahead.x - origin.x, ahead.y - origin.y)
    val obstacle =
      physWorld.collideAsCircle(this, ahead, collBody.getRadiusScaled)
    Vector2e.free(visionPos) //free the memory

    //draws debug circle
    //MainGame.debugRender.circle(ahead.x, ahead.y, collBody.getRadiusScaled)

    //calculate the force opposite to obstacle center
    obstacle.foreach(o => ((avoid ++ pos) -- o.collBody.center).nor ** maxForceAvoid / mass)
    Vector2e.free(ahead) //free the memory

    obstacle.isDefined //returns true if collided
  }

  override def draw(shapeRender: ShapeRenderer): Unit = {
    if (visible) {
      //shapeRender.polygon(collPolygon.getTransformedVertices)
      //shapeRender.circle(pos.x, pos.y, 5)
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
    val bulletPos = Vector2e.pool(movingForce).nor ** (sWidth + BasicBullet.radius) ++ pos
    val bullet = BasicBullet.create(this, physWorld,
      bulletPos, Vector2e.pool(movingForce).nor ** (maxSpeed * 5),
      owner.colorIndex)

    //sets the bullet statistics
    bullet.damage = damage
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

