package game.main.objects.improved

import com.badlogic.gdx.math.Vector2
import game.main.physics.CollisionHandler
import game.main.physics.collision.CollisionBody
import game.main.players.Player
import game.main.units.BulletCreator
import game.util.Vector2e

class UnitObject(physWorld: CollisionHandler, collBody: CollisionBody) extends PhysicsObject(physWorld, collBody)
{

  var owner: Player = _

  //movement stats
  var maxMovingForce: Float = 0.033f
  val movingForce: Vector2 = Vector2e(0f, 0f)
  val moveTarget: Vector2 = Vector2e(0f, 0f)

  //basic stats & shooting
  var health: Float = _

  override def update(): Unit = {
    updatePhysics()
    super.update()
    updateMovement()
  }

  /** Updates the movement of the object. */
  def updateMovement(): Unit = {
    val limit = maxMovingForce
    movingForce.limit(limit) //limit speed
    velocity.add(movingForce) //add movement to physic engine
  }

  /** Reduces the health of the object and checks if object has diead */
  def reduceHealth(damage: Float): Unit = {
    health -= damage
    if (health <= 0) this.delete()
  }

  /** Returns the max speed of the object */
  def maxSpeed: Float =
    maxMovingForce * (friction + physWorld.globalFriction) /
      (friction + physWorld.globalFriction - 1)


}
