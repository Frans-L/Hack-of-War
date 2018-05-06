package game.main.gameworld.gameobject.objects

import com.badlogic.gdx.math.Vector2
import game.main.gameworld.collision
import game.main.gameworld.collision.CollisionHandler
import game.main.gameworld.collision.bodies.CollisionBody
import game.main.gameworld.gameobject.objects.UnitObject.{AIScore, AIScoreNone}
import game.main.gameworld.gameobject.objects.UnitObject.State.Value
import game.main.players.Player
import game.util.Vector2e

object UnitObject {

  val noHitTick: Long = -1

  //state of the object
  object State extends Enumeration {
    type State = Value
    val normal, attack = Value
  }

  object Category extends Enumeration {
    type State = Value
    val none, soldier, tank, building = Value
  }

  /** How AI determines which unit to use. */
  abstract class AIScore {
    val attackLight: Float //0-100
    val attackHeavy: Float //0-100
    val light: Float //0-1
    val heavy: Float //0-1
    val priority: Float // > 0

    val string: String //easier printing
    override def toString: String = string
  }

  val AIScoreNone: AIScore = new AIScore {
    override val priority: Float = 0
    override val attackLight: Float = 0
    override val heavy: Float = 0
    override val light: Float = 0
    override val attackHeavy: Float = 0
    override val string = "None"
  }
}

class UnitObject(physWorld: CollisionHandler, collBody: CollisionBody, var owner: Player) extends PhysicsObject(physWorld, collBody) {

   //is set by unitCreators
  var category: UnitObject.Category.Value = UnitObject.Category.none
  var state: UnitObject.State.Value = UnitObject.State.normal
  var aiScore: AIScore = AIScoreNone

  //movement stats
  var maxMovingForce: Float = 0.033f
  val movingForce: Vector2 = Vector2e(0f, 0f)
  val moveTarget: Vector2 = Vector2e(0f, 0f)

  //basic stats & shooting
  var health: Float = 100
  var lastHitTick: Long = UnitObject.noHitTick //no hit

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
    lastHitTick = ticker.total
    if (health <= 0) this.delete()
  }

  /** Returns the max speed of the object */
  def maxSpeed: Float =
    maxMovingForce * (friction + physWorld.globalFriction) /
      (friction + physWorld.globalFriction - 1)


}
