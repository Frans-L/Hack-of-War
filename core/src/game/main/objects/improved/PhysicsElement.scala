package game.main.objects.improved

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import game.GameElement
import game.main.MainGame
import game.main.physics.{ObjectType, CollisionHandler}
import game.main.physics.collision.CollisionBody
import game.util.Vector2e
import game.util.pools.MinimumTranslationVectorPool

import scala.collection.mutable

class PhysicsElement(var physWorld: CollisionHandler, var collBody: CollisionBody) {

  /*
  var mass: Float = 100f //default values
  var friction: Float = 0.25f
  val velocity = Vector2e(0, 0)

  var collToMe: Boolean = true //if others objects checks collision with this object
  var collToOthers: Boolean = true //if this object checks collision with other object

  //this object checks collision only with these filtered objects, if Option is defined
  var collFilter: mutable.Buffer[GameElement] = mutable.Buffer.empty

  override def update(delta: Int): Unit = {

    updateCollPolygon(collBody) //update collbodies location etc.
    updatePhysics() //updates fathers physics

    super.update(delta)
  }

  override def draw(shapeRenderer: ShapeRenderer): Unit = {
    if(MainGame.drawCollBox) collBody.draw(shapeRenderer)
  }


  /** Updates physics */
  private def updatePhysics(): Unit = {
    var crash = false

    //checks collision
    if (collToOthers) {
      val collForce = MinimumTranslationVectorPool.obtain()
      val crashObj = physWorld.collide(this, collForce, collFilter)
      crashObj.foreach(obj => crash = collision(obj, collForce))
      MinimumTranslationVectorPool.free(collForce) //free the memory
    }

    //if free to move
    if (!crash) {
      parent.pos.mulAdd(velocity, parent.ticker.delta)
    }

    //reduces the velocity if the total friction isn't zero
    val fric = friction + physWorld.globalFriction
    if (fric != 0) velocity.scl(1f / fric)

    updateCollPolygon(collBody) //update collisionBox coords
  }


  /** Updates collPolygons location, rotation and scale.
    * updatePhysics calls this automatically. */
  def updateCollPolygon(body: CollisionBody): Unit = {
    body.setPosition(parent.pos.x - parent.origin.x, parent.pos.y - parent.origin.y)
    body.setScale(parent.scale.x, parent.scale.y)
    body.setRotation(parent.angle)
    body.setOrigin(parent.origin.x, parent.origin.y)
  }

  /** Will be called when collision happened by updatePhysics.
    * Thse default action is to move out of the object and "bounce" back.
    *
    * @return True if the collision was accepted (default) */
  protected def collision(crashObj: PhysicsElement, collForce: MinimumTranslationVector): Boolean = {
    parent.pos.mulAdd(collForce.normal, collForce.depth + 0.1f) //move out of the collision
    velocity.setAngle(collForce.normal.angle()) //bounce back
    true
  }

  */
}
