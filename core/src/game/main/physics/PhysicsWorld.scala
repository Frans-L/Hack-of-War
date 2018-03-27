package game.main.physics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import com.badlogic.gdx.math.{Intersector, Vector2}
import game.GameElement
import game.main.Player
import game.main.physics.objects.ObjectType

import scala.collection.mutable


/**
  * Created by Frans on 11/03/2♦018.
  *
  * Handles the physics and collision
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
      if (Intersector.overlapConvexPolygons(obj.collBody, o.collBody, mtv))
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

  /** Returns the collided object, and the angle of the polyline */
  def collideAsCircle(obj: ObjectType, center: Vector2, radius: Float):
  (Option[ObjectType], Float) = {

    var result: (Boolean, Float) = (false, 0f) //(is collided, angle)
    var crashObj: Option[ObjectType] = None
    for ((owner, o) <- mapBufferIterator(units) if crashObj.isEmpty && o != obj) {
      result = o.collBody.overlapsCircle(center, radius)
      if (result._1) crashObj = Some(o)
    }

    (crashObj, result._2)
  }

  /** Returns the collided object, and the angle of the polyline */
  def collideAsCircle(obj: ObjectType): (Option[ObjectType], Float) = {
    collideAsCircle(obj, obj.collBody.center, obj.collBody.getRadius)
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

  /** Iterates map's every buffer. */
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
