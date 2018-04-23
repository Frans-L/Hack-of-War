package game.main.physics

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import game.GameElement
import game.util.Vector2e
import game.util.Vector2e._

/**
  * Created by Frans on 26/02/2018.
  */
trait SpriteType extends GameElement {

  var visible: Boolean = true
  var enabled: Boolean = true
  var deleted: Boolean = false

  val pos: Vector2
  val size: Vector2
  val origin: Vector2 = Vector2e(0, 0)
  val scale: Vector2 = Vector2e(1f, 1f)
  var angle: Float = 0f //degrees

  var sprite: Sprite

  /** Updates sprite's location, rotation and size. (Public) */
  def updateSprite(): Unit = {
    updateSprite(sprite)
  }

  /** Updates sprite's location, rotation and size */
  protected def updateSprite(s: Sprite): Unit = {
    s.setOrigin(origin.x, origin.y)
    s.setBounds(pos.x - origin.x, pos.y - origin.y, size.width, size.height)
    s.setScale(scale.x, scale.y)
    s.setRotation(angle)
  }


  /** Returns scaled width */
  def sWidth: Float = size.width * scale.x

  /** Returns scaled height */
  def sHeight: Float = size.height * scale.y

  /** Get X location next to this object */
  def nextToX: Float = pos.x + sWidth

  /** Get Y location next to this object */
  def nextToY: Float = pos.y + sHeight

}
