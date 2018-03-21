package game.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite, TextureRegion}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.{Interpolation, Polygon, Vector2}
import game.Ticker
import game.main.{CollisionBody, CollisionHandler, MainGame}
import game.util.{Utils, Vector2e, Vector2mtv}
import game.util.Vector2e._

/**
  * Created by Frans on 26/02/2018.
  */
trait PhysicsObject extends GeneralObject {

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

  //activeObject's origin is at the center
  override val origin: Vector2 = Vector2e(size.x / 2f, size.y / 2f)

  //physics to gameObject
  val collHandler: CollisionHandler //the collision handler
  val collBody: CollisionBody
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
  def destroy(): Unit = Unit


}

