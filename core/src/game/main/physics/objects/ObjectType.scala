package game.main.physics.objects

import com.badlogic.gdx.math.Vector2
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

  var collOn = true //if the collision is checked by others object
  val collBody: CollisionBody //the collision body
  val velocity: Vector2
  val mass: Float

  /**
    * Updates collPolygons location, rotation and scale
    */
  def updateCollPolygon(): Unit = {
    collBody.setPosition(pos.x - origin.x, pos.y - origin.y)
    collBody.setScale(scale.x, scale.y)
    collBody.setRotation(angle)
    collBody.setOrigin(origin.x, origin.y)
  }

  /** ActiveObject should be destroyable */
  def destroy(): Unit = {
    deleted = true
  }


}

