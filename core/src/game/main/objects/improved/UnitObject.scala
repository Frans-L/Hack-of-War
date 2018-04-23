package game.main.objects.improved

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import game.GameElement
import game.main.objects.brains.UnitElement
import game.main.physics.collision.CollisionBody
import game.main.players.Player
import game.main.units.BulletCreator
import game.util.Vector2e
import game.util.Vector2e._

import scala.collection.mutable

class UnitObject() extends GameObject {

  var owner: Player = _
  var physics: PhysicsElement = _

  val movingForce: Vector2 = Vector2e(0f, 0f)
  var maxMovingForce: Float = 0.033f
  val moveTarget: Vector2 = Vector2e(0f, 0f)

  //basic stats & shooting
  var health: Float = _
  var damage: Float = _
  var bulletCreator: BulletCreator = _

  override def update(delta: Int): Unit = {
    updateMovement()
    super.update(delta)
  }

  /** Updates the movement of the object. */
  def updateMovement(): Unit = {
    val limit = maxMovingForce
    movingForce.limit(limit) //limit speed
    physics.velocity.add(movingForce) //add movement to physic engine
  }

  /** Shoots a bullet */
  def shoot(): Unit = {
    /*
    //calculates the pos of the bullet and create it
    val bulletPos = Vector2e(movingForce).nor ** (sWidth / 2f + bulletCreator.radius) ++ pos
    val bullet = bulletCreator.create(this, physWorld,
      bulletPos, Vector2e(movingForce).nor ** (maxSpeed * 5),
      owner.colorIndex)

    //sets the bullet statistics
    bullet.damage = damage
    bullet.collFilter ++= owner.enemies.asInstanceOf[mutable.Buffer[GameElement]]
    bullet.collFilter += physWorld.map
    */
  }

  /** Reduces the health of the object and checks if object has diead */
  def reduceHealth(damage: Float): Unit = {
    health -= damage

    //if (health <= 0) destroy()
  }

  /** Returns the max speed of the object */
  def maxSpeed: Float =
    maxMovingForce * (physics.friction + physics.physWorld.globalFriction) /
      (physics.friction + physics.physWorld.globalFriction - 1)

  def addElement(unitElement: UnitElement): UnitObject.this.type = {
    unitElement.pUnit = this
    super.addElement(unitElement)
  }

}
