package game.main.objects.improved

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import game.GameElement
import game.util.Vector2e

import scala.collection.mutable

class GameObject extends GameElement with ObjectElement {

  val pos: Vector2 = Vector2e(0, 0)
  val size: Vector2 = Vector2e(0, 0)
  val scale: Vector2 = Vector2e(1, 1)
  val origin: Vector2 = Vector2e(0, 0)
  var angle: Float = 0

  var canBeDeleted: Boolean = false

  override def update(): Unit = {
    update(ticker.delta)
  }

  override def update(delta: Int): Unit = {
    elements.foreach(_.update(delta))
  }

  /** Adds a new child to a object relatively to this object. */
  def addElementRelatively(gameObject: GameObject): this.type = {
    gameObject.pos.add(pos.x - origin.x, pos.y - origin.y)
    gameObject.scale.scl(scale)
    gameObject.angle += angle
    elements += gameObject
    this
  }

  /** Adds a new child element to this object. */
  override def addElement(objectElement: ObjectElement): GameObject.this.type = {
    objectElement.setParent(this)
    elements += objectElement
    this
  }

  /*
  def rPos: Vector2 = pPos + father.for

  override def size: Vector2 = pSize

  override def scale: Vector2 = pScale

  override def origin: Vector2 = pOrigin

  override def angle: Float = pAngle

  override def angle_= (v: Float): Unit = pAngle = v
  */


  def sHeight: Float = scale.y * size.y

  def sWidth: Float = scale.x * size.x

  def nextToX: Float = pos.x + sWidth

  def nextToY: Float = pos.y + sHeight

  override def setParent(f: GameObject): Unit = {
    parent = f
  }

}
