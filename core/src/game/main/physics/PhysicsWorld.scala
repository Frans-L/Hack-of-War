package game.main.physics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import com.badlogic.gdx.math.{Intersector, Vector2}
import game.GameElement
import game.main.Player
import game.main.physics.objects.ObjectType
import game.util.Vector2mtv

import scala.collection.mutable


/**
  * Created by Frans on 11/03/2♦018.
  *
  * Updates all ObjectTypes that are linked to the LinkedHashMap units.
  * units(owner) = mutable.Buffer[ObjectType]
  * Owner can be any GameElement. So each object will be always linked to its owner GameElement.
  *
  * This makes possible to update only the objects of the certain category.
  * For instance, if it's needed to find collision with an enemy,
  * only tge objects of the enemy can be selected.
  *
  * Linkedhashmap saves also the order of the owners. So the objects that are added to newer
  * owner will be drawn later. => New objects, with a new owner, will be
  * drawn on top of everything.
  *
  */
class PhysicsWorld(val map: Map) extends GameElement {

  private val units: mutable.LinkedHashMap[GameElement, mutable.Buffer[ObjectType]] =
    mutable.LinkedHashMap[GameElement, mutable.Buffer[ObjectType]]()

  units.put(map, map.collObjects.asInstanceOf[mutable.Buffer[ObjectType]]) //add map collisions

  /** Updates all units that are added to this collisionWorld */
  override def update(): Unit = {
    for ((owner, obj) <- mapBufferIterator(units)) {
      obj.update()
    }

    clearDeletedUnits()
  }

  /** Draws all units that are added to this collisionWorld */
  override def draw(batch: Batch): Unit = {
    for ((owner, obj) <- mapBufferIterator(units)) {
      obj.draw(batch)
    }
  }

  override def draw(shapeRender: ShapeRenderer): Unit = ???


  /** Removes deleted units */
  private def clearDeletedUnits(): Unit = {
    for (owner <- units) {
      //removes deleted units
      for (i <- owner._2.indices.reverse) {
        if (owner._2(i).deleted) owner._2.remove(i)
      }

      //removes empty owners
      //TODO not sure if safe way to delete?
      if (owner._2.isEmpty) units.remove(owner._1)
    }
  }

  /** Checks if the obj is colliding with something.
    *
    * @param obj the collisionBody of the object
    * @param mtv sets the minimumTranslationVector that is required to separate objects
    * @return the body of the collided object
    */
  def collide(obj: ObjectType, mtv: MinimumTranslationVector = null): Option[ObjectType] = {
    var crashObj: Option[ObjectType] = None
    for ((owner, o) <- mapBufferIterator(units) if crashObj.isEmpty && o != obj) {
      if (obj.collBody.overlaps(o.collBody, mtv))
        crashObj = Some(o)
    }

    crashObj
  }

  /** Returns true if collided */
  def isCollided(obj: ObjectType, mtv: MinimumTranslationVector = null): Boolean = {
    collide(obj, mtv).isDefined
  }


  /** Returns the object that is collided with the point */
  def collidePoint(excludeObj: ObjectType, point: Vector2): Option[ObjectType] = {
    var coll = false
    var crashObj: Option[ObjectType] = None
    for ((owner, o) <- mapBufferIterator(units) if crashObj.isEmpty && o != excludeObj) {
      coll = o.collBody.contains(point.x, point.y)
      if (coll) crashObj = Some(o)
    }

    crashObj
  }

  /** Returns the collided object */
  def collideAsCircle(obj: ObjectType, center: Vector2, radius: Float):
  Option[ObjectType] = {

    val mtvTMP = Vector2mtv.pool()
    var result: Boolean = false //(is collided, angle)
    var crashObj: Option[ObjectType] = None
    for ((owner, o) <- mapBufferIterator(units) if crashObj.isEmpty && o != obj) {
      result = o.collBody.overlapsCircle(center, radius, mtvTMP)
      if (result) crashObj = Some(o)
    }

    Vector2mtv.free(mtvTMP) //frees the memory

    crashObj
  }

  /** Returns the collided object, and the angle of the polyline */
  def collideAsCircle(obj: ObjectType): Option[ObjectType] = {
    collideAsCircle(obj, obj.collBody.center, obj.collBody.getRadiusScaled)
  }


  /** Adds a new unit to the update list. The owner has to exist! */
  def addUnit(owner: GameElement, obj: ObjectType): Unit = {
    if (units.contains(owner)) units(owner) += obj
    else units.put(owner, mutable.Buffer[ObjectType](obj))
  }

  /** Removes a new unit from the update list. The owner has to exist! */
  def removeUnit(owner: GameElement, obj: ObjectType): Unit = {
    units(owner) -= obj
    if (units(owner).isEmpty) units.remove(owner)
  }

  /** Iterates every T of the
    *  mutable.LinkedHashMap[GameElement, mutable.Buffer[T]] */
  private def mapBufferIterator[T]
  (b: mutable.Map[GameElement, mutable.Buffer[T]]): Iterator[(GameElement, T)] =
    new Iterator[(GameElement, T)] {

      private val categoryI = b.iterator
      private var owner: GameElement = _ //will be given a value at first call of next()
      private var bodyI: Iterator[T] = Iterator[T]()

      override def hasNext: Boolean = bodyI.hasNext || categoryI.hasNext

      override def next(): (GameElement, T) = {
        if (!bodyI.hasNext) {
          val nxt = categoryI.next
          owner = nxt._1
          bodyI = nxt._2.iterator
        }

        (owner, bodyI.next())
      }
    }

}
