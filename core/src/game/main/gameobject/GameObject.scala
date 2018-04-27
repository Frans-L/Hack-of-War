package game.main.gameobject

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import game.GameElement
import game.util.Vector2e

import scala.collection.mutable

class GameObject() extends GameElement with ObjectElement {

  val pos: Vector2 = Vector2e(0, 0)
  val size: Vector2 = Vector2e(0, 0)
  val scale: Vector2 = Vector2e(1, 1)
  val origin: Vector2 = Vector2e(0, 0)
  var angle: Float = 0

  var canBeDeleted: Boolean = false
  val elements: mutable.ListBuffer[ObjectElement] = mutable.ListBuffer.empty

  //avoid throwing objects to garbageCollector
  lazy private val relativePos = Vector2e(0, 0)
  lazy private val relativeScale = Vector2e(0, 0)

  /** Updates the object and all it's elements */
  override def update(): Unit = {
    elements.foreach(_.update(this, ticker.delta))
  }

  override def draw(shapeRender: ShapeRenderer): Unit = elements.foreach(_.draw(this, shapeRender))

  override def draw(batch: Batch): Unit = elements.foreach(_.draw(this, batch))

  /** Updates the pos relatively to parent object */
  override def update(parent: GameObject, delta: Int): Unit =
    runRelatively(parent, update)

  /** Draws the object relatively to parent. */
  override def draw(parent: GameObject, batch: Batch): Unit =
    runRelatively(parent, () => draw(batch))

  override def draw(parent: GameObject, shapeRenderer: ShapeRenderer): Unit =
    runRelatively(parent, () => draw(shapeRenderer))


  /** Calls the method 'run' so that position of this object is relative to parent. */
  private def runRelatively(parent: GameObject, run: () => Unit): Unit = {
    //sets the position to be relative
    relativePos.set(pos)
    relativeScale.set(scale)
    val orgAngle = angle
    pos.add(parent.pos.x - parent.origin.x, parent.pos.y - parent.origin.y)
    scale.scl(parent.scale)
    angle += parent.angle

    run() //updates with its real coords

    //sets back to its own original position
    pos.set(relativePos)
    scale.set(relativeScale)
    angle = orgAngle
  }


  def sHeight: Float = scale.y * size.y

  def sWidth: Float = scale.x * size.x

  def nextToX: Float = pos.x + sWidth

  def nextToY: Float = pos.y + sHeight

  /** Appends new element to object's elements */
  def appendElement[T <: ObjectElement](objectElement: T): this.type = {
    objectElement.checkParent(this)
    elements.append(objectElement)
    this
  }

  /** Prepends new element to object's elements */
  def prependElement[T <: ObjectElement](objectElement: T): this.type = {
    objectElement.checkParent(this)
    elements.prepend(objectElement)
    this
  }

  override def checkParent(parent: GameObject): Unit = Unit //anything works

  /** Marks that the object can be deleted. */
  override def delete(): Unit = {
    canBeDeleted = true
  }

}