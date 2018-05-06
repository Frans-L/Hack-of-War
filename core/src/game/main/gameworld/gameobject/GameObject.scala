package game.main.gameworld.gameobject

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import game.GameElement
import game.util.Vector2e

import scala.collection.mutable
import scala.reflect.ClassTag

class GameObject() extends GameElement with ObjectElement {

  val pos: Vector2 = Vector2e(0, 0)
  val size: Vector2 = Vector2e(0, 0)
  val scale: Vector2 = Vector2e(1, 1)
  val origin: Vector2 = Vector2e(0, 0)
  var angle: Float = 0 //degrees
  var opacity: Float = 1f //values between 0-1, can be used with textures

  var canBeDeleted: Boolean = false
  val elements: mutable.ListBuffer[ObjectElement] = mutable.ListBuffer.empty

  //extra actions when unit is deleted
  private lazy val deleteActions: mutable.ListBuffer[() => Unit] = mutable.ListBuffer.empty

  //parent of the this object, updated in each relative call
  private var objectParent: Option[GameObject] = None

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

    objectParent = Some(parent) //sets the parent object
    val orgPosAngle = pos.angle //needed to keep pos angle same
    val orgOpacity = opacity //needed when parent's opacity is zero
    val orgScaleX = scale.x //needed when parent's scale is zero
    val orgScaleY = scale.y

    //sets to relative coords
    pos.setAngle(pos.angle + parent.angle)
    pos.add(parent.pos.x, parent.pos.y)
    scale.scl(parent.scale.x, parent.scale.y)
    angle += parent.angle
    opacity *= parent.opacity

    run() //updates with its real coords

    //sets back to its own original position
    pos.sub(parent.pos.x, parent.pos.y)
    angle -= parent.angle
    pos.setAngle(orgPosAngle)
    opacity = if (parent.opacity != 0) opacity / parent.opacity else orgOpacity

    if (parent.scale.x != 0 && parent.scale.y != 0) scale.scl(1f / parent.scale.x, 1f / parent.scale.y)
    else scale.set(orgScaleX, orgScaleY)

    //clears the parent object, so if this object is called without relativity
    //there will be no parents
    objectParent = None
  }

  /** Returns the parent, if it exists. */
  def getParent: Option[GameObject] = objectParent

  /** Returns the parent, or itself */
  def grandParent: GameObject = {
    objectParent.map(_.grandParent).getOrElse(this)
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

  /** Returns the first element */
  def headElement[T <: ObjectElement : ClassTag]: T = {
    val classTag = implicitly[ClassTag[T]].runtimeClass
    require(classTag.isInstance(elements.head), "headElement isn't instance of T")
    elements.head.asInstanceOf[T]
  }

  /** Returns the first element */
  def lastElement[T <: ObjectElement : ClassTag]: T = {
    val classTag = implicitly[ClassTag[T]].runtimeClass
    require(classTag.isInstance(elements.last), "lastElement isn't instance of T")
    elements.last.asInstanceOf[T]
  }

  /** Checks if the parent is valid for this object. */
  override def checkParent(parent: GameObject): Unit = Unit //anything works

  /** Adds new deadAction */
  def addDeleteAction(action: () => Unit): Unit = {
    deleteActions += action
  }

  /** Marks that the object can be deleted. */
  override def delete(): Unit = {
    if (!canBeDeleted) deleteActions.foreach(_ ()) //call each action once
    canBeDeleted = true
  }

}