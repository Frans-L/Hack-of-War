package game.main.physics.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import com.badlogic.gdx.math.Vector2
import game.main.physics.PhysicsWorld
import game.main.physics.collision.{CollisionBody, PolygonBody}
import game.util.Vector2e

/**
  * Created by Frans on 26/02/2018.
  */
trait ObjectType extends SpriteType {

  //all variables that are inherited
  /*
  override var visible: Boolean = true
  override var enabled: Boolean = true
  override var deleted: Boolean = false

  override var sprite: Sprite

  override val size: Vector2
  override val scale: Vector2 = Vector2e(1f, 1f)
  override val origin: Vector2 = Vector2e(0, 0)

  override val pos: Vector2
  override var angle: Float = 0f //degrees

  */

  //physics to gameObject

  val physWorld: PhysicsWorld
  var mass: Float
  var friction: Float

  var collToMe: Boolean = true //if others objects checks collision with this object
  var collToOthers: Boolean = true //if this object checks collision with other objects
  val collBody: CollisionBody //the collision body

  val velocity: Vector2 = Vector2e(0f, 0f)


  /** Updates physics */
  def updatePhysics(): Unit = {

    pos.mulAdd(velocity, ticker.delta)
    velocity.scl(1f / (friction + physWorld.globalFriction))

    updateCollPolygon()
  }

  /** Updates collPolygons location, rotation and scale */
  protected def updateCollPolygon(): Unit = {
    collBody.setPosition(pos.x - origin.x, pos.y - origin.y)
    collBody.setScale(scale.x, scale.y)
    collBody.setRotation(angle)
    collBody.setOrigin(origin.x, origin.y)
  }

  /** ActiveObject should be destroyable */
  def destroy(): Unit = {
    deleted = true
  }


  /** Adds a force to the object  */
  def addForce(force: Vector2): Unit = {
    velocity.mulAdd(force, 1f / mass) // F = MA => A = F/M
    Gdx.app.log("objecttype", "" + velocity)
  }

}

