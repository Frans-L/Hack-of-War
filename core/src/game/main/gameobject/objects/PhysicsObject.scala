package game.main.gameobject.objects

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import game.GameElement
import game.main.MainGame
import game.main.gameobject.GameObject
import game.main.physics.CollisionHandler
import game.main.physics.collision.CollisionBody
import game.util.Vector2e
import game.util.Vector2e._
import game.util.pools.VectorPool

import scala.collection.mutable

class PhysicsObject(var physWorld: CollisionHandler,
                    val collBody: CollisionBody) extends GameObject {

  var mass: Float = 100f
  var friction: Float = 0.25f //values > 0
  var elasticity: Float = 0.8f //values between 0-1
  val velocity = Vector2e(0, 0)
  var collided: Boolean = false //will be reset every frame
  var static: Boolean = false

  //this object checks collision only with these filtered objects, if Option is defined
  var collFilter: mutable.Buffer[GameElement] = mutable.Buffer.empty

  override def update() {
    updatePhysics()
    super.update()
  }

  protected def updatePhysics(): Unit = {
    //reduces the velocity if the total friction isn't zero
    val fric = friction + physWorld.globalFriction
    if (fric != 0) velocity.scl(1f / fric)

    pos.mulAdd(velocity, ticker.delta) //move the object

    updateCollPolygon(collBody) //update collbodies
    collided = false
  }

  /** Updates collPolygons location, rotation and scale.
    * updatePhysics calls this automatically. */
  def updateCollPolygon(body: CollisionBody): Unit = {
    body.setOrigin(origin.x, origin.y)
    body.setPosition(pos.x - origin.x, pos.y - origin.y)
    body.setScale(scale.x, scale.y)
    body.setRotation(angle)
  }

  /** The action when the object collides with something.
    * @return True if the collision was accepted (default) */
  def collision(crashObj: PhysicsObject, dir: Vector2, depth: Float): Boolean = {
    if (!static) {
      pos.mulAdd(dir, depth + 0.02f)
      velocity.scl(crashObj.elasticity * elasticity)
      velocity.setAngle(dir.angle)
    }
    collided = true
    true
  }

  /** Adds an impact to the object that will add a force */
  def addImpact(vel: Vector2, mass2: Float): Unit = {
    val speed =
      VectorPool.obtain(velocity).scl(-math.abs(math.cos(velocity.angleRad(vel))).toFloat) ++ vel
    velocity.mulAdd(speed, mass2 / mass)
    VectorPool.free(speed)
  }

  /** Draws the collision boxes */
  override def draw(shapeRender: ShapeRenderer): Unit = {
    if (MainGame.drawCollBox) {
      collBody.draw(shapeRender)
      super.draw(shapeRender)
    }
  }

}
