package game.main.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.{Interpolation, MathUtils, Vector2}
import com.badlogic.gdx.utils.{Pool, Pools}
import game.GameElement
import game.loader.{GameTextures, UnitTextures}
import game.main.MainGame
import game.main.physics.{ObjectType, CollisionHandler}
import game.main.physics.collision.{CollisionBody, PolygonBody}
import game.main.units.{BasicBullet, BulletCreator}
import game.main.players.Player
import game.main.gameMap.Path
import game.main.objects.brains.Brain
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
  override var physWorld: CollisionHandler = _
  override var collBody: CollisionBody = _

  override var sprite: Sprite = _

  val brains: mutable.Buffer[Brain] = mutable.Buffer.empty
  val moveTarget: Vector2 = Vector2e(0f, 0f)

  //steering stats
  val movingForce: Vector2 = Vector2e(0f, 0f)
  var maxMovingForce: Float = _
  var steeringPath: Option[UnitPath] = None

  //basic stats & shooting
  var health: Float = _
  var damage: Float = _
  var bulletCreator: BulletCreator = _

  var maxForwardForceAttack: Float = _ //speed when attacking


  def this(textures: UnitTextures, size: Vector2, owner: Player,
           physWorld: CollisionHandler, collBody: CollisionBody) {
    this()
    init(textures, size, owner, physWorld, collBody) // sets all variables a value
  }

  private def mousePos: Vector2 = MainGame.debugViewPort.unproject(Vector2e(Gdx.input.getX, Gdx.input.getY))


  /** Initializes the object. Have to called if obtained from the pool! */
  def init(textures: UnitTextures, size: Vector2, owner: Player,
           physWorld: CollisionHandler, collBody: CollisionBody): UnitObject.this.type = {

    setToDefault(textures, size, owner, physWorld, collBody)

    updateCollPolygon(collBody) //updates collisionbox
    updateSprite() //updates sprites
    //physWorld.addUnit(owner.asInstanceOf[GameElement], this) //adds update calls

    this
  }

  /** Initializes the all variables of the object.
    * Makes pooling possible. */
  def setToDefault(textures: UnitTextures, size: Vector2, owner: Player,
                   physWorld: CollisionHandler, collBody: CollisionBody): Unit = {

    this.enabled = true
    this.visible = true
    this.canBeDeleted = false

    this.owner = owner
    this.physWorld = physWorld
    this.collBody = collBody

    brains.clear()
    moveTarget.set(0, 0)

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

    //movement
    this.movingForce.set(0, 0)
    this.maxMovingForce = 0.033f

    this.health = 100f
    this.damage = 30f
    this.bulletCreator = BasicBullet

  }

  /**
    * Updates the object collision, physics, AI etc...
    */
  override def update(): Unit = {
    if (enabled) {

      brains.foreach(b => b.update(this)) //update brains

      //if (pos.x > 1920 / 2f - 150) destroy() //TODO because of debug

      updateMovement()
      updatePhysics()
      updateSprite()

    }
  }

  /** Updates the movement of the object. */
  def updateMovement(): Unit = {
    val limit = maxMovingForce
    movingForce.limit(limit) //limit speed
    velocity.add(movingForce) //add movement to physic engine
  }

  /** Shoots a bullet */
  def shoot(): Unit = {
      /*
      //calculates the pos of the bullet and create it
      val bulletPos = Vector2e(movingForce).nor ** (sWidth / 2f + bulletCreator.radius) ++ pos
      val bullet = bulletCreator.create(this, objectHandler,
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

    if (health <= 0) destroy()
  }

  /** Returns the max speed of the object */
  def maxSpeed: Float =
    maxMovingForce * (friction + physWorld.globalFriction) /
      (friction + physWorld.globalFriction - 1)

  /** Resets the object */
  override def reset(): Unit = {
    setToDefault(null, null, null, null, null)
    canBeDeleted = true
  }

  /** Frees the object back to pool. */
  override def delete(): Unit = {
    UnitObject.pool.free(this)
  }

}

