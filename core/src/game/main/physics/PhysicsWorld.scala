package game.main.physics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import com.badlogic.gdx.math.{Intersector, Vector2}
import game.GameElement
import game.main.physics.collision.CollisionBody
import game.main.players.Player
import game.util.{Dimensions, Vector2e, Vector2mtv}

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
  * only the objects of the enemy can be selected.
  *
  * Linkedhashmap saves also the order of the owners. The objects that are added to newer
  * owner will be drawn later. So new objects, with a new owner, will be
  * drawn on top of everything.
  *
  */
class PhysicsWorld(val dimensions: Dimensions) extends GameElement {

  //global physics stats
  var globalFriction: Float = 1f
  var globalShadowPos: Vector2 = Vector2e(-3f, -5f)

  var map: game.main.gameMap.Map = _ //TODO temporary solution, to add map

  private val units: mutable.LinkedHashMap[GameElement, mutable.Buffer[ObjectType]] =
    mutable.LinkedHashMap[GameElement, mutable.Buffer[ObjectType]]()

  private lazy val tmpEmptyBuffer = mutable.Buffer.empty[GameElement] //caches an empty buffer

  private lazy val tmpMapBuffer = mutable.Buffer[GameElement](map)

  /** Updates all units that are added to this collisionWorld */
  override def update(): Unit = {
    for ((owner, obj) <- mapBufferIterator(units, tmpEmptyBuffer)) {
      obj.update()
    }

    clearDeletedUnits()
  }

  /** Draws all units that are added to this collisionWorld */
  override def draw(batch: Batch): Unit = {
    for ((owner, obj) <- mapBufferIterator(units, tmpEmptyBuffer)) {
      obj.draw(batch)
    }
  }

  override def draw(shapeRender: ShapeRenderer): Unit = {
    for ((owner, obj) <- mapBufferIterator(units, tmpEmptyBuffer)) {
      obj.draw(shapeRender)
    }
  }


  /** Removes deleted units */
  private def clearDeletedUnits(): Unit = {
    for (owner <- units) {
      //removes deleted units
      for (i <- owner._2.indices.reverse) {
        if (owner._2(i).deleted) owner._2.remove(i)
      }

      //removes empty owners
      //TODO not sure if safe way to delete? it seems like it safe...
      if (owner._2.isEmpty) units.remove(owner._1)
    }
  }

  /** Checks if the obj is colliding with something.
    *
    * @param obj the collisionBody of the object
    * @param mtv sets the minimumTranslationVector that is required to separate objects, can be null
    * @return the body of the collided object
    */
  def collide(obj: ObjectType, mtv: MinimumTranslationVector,
              category: mutable.Buffer[GameElement]): Option[ObjectType] = {
    var crashObj: Option[ObjectType] = None
    for ((owner, o) <- mapBufferIterator(units, category) if crashObj.isEmpty && o != obj && o.collToMe) {
      if (obj.collBody.overlaps(o.collBody, mtv))
        crashObj = Some(o)
    }

    crashObj
  }

  /** Returns true if collided */
  def isCollided(obj: ObjectType, mtv: MinimumTranslationVector,
                 category: mutable.Buffer[GameElement]): Boolean = {
    collide(obj, mtv, category).isDefined
  }


  /** Returns the object that is collided with the point */
  def collidePoint(excludeObj: ObjectType, point: Vector2,
                   category: mutable.Buffer[GameElement]): Option[ObjectType] = {
    var coll = false
    var crashObj: Option[ObjectType] = None
    for ((owner, o) <- mapBufferIterator(units, category) if crashObj.isEmpty && o != excludeObj && o.collToMe) {
      coll = o.collBody.contains(point.x, point.y)
      if (coll) crashObj = Some(o)
    }

    crashObj
  }

  /** Returns the object that is collided with the collisionBody */
  def collideCollisionBody(excludeObj: ObjectType, collBody: CollisionBody,
                           mtv: MinimumTranslationVector,
                           category: mutable.Buffer[GameElement]): Option[ObjectType] = {

    var crashObj: Option[ObjectType] = None
    for ((owner, o) <- mapBufferIterator(units, category) if crashObj.isEmpty && o != excludeObj && o.collToMe) {
      if (collBody.overlaps(o.collBody, mtv))
        crashObj = Some(o)
    }
    crashObj
  }

  /** Returns the collided object */
  def collideCircle(obj: ObjectType, center: Vector2, radius: Float,
                    category: mutable.Buffer[GameElement]):
  Option[ObjectType] = {

    val mtvTMP = Vector2mtv.pool()
    var result: Boolean = false //(is collided, angle)
    var crashObj: Option[ObjectType] = None
    for ((owner, o) <- mapBufferIterator(units, category) if crashObj.isEmpty && o != obj && o.collToMe) {
      result = o.collBody.overlapsCircle(center, radius, mtvTMP)
      if (result) crashObj = Some(o)
    }

    Vector2mtv.free(mtvTMP) //frees the memory

    crashObj
  }

  /** Returns the collided object, and the angle of the polyline */
  def collideCircle(obj: ObjectType,
                    category: mutable.Buffer[GameElement]): Option[ObjectType] = {
    collideCircle(obj, obj.collBody.center, obj.collBody.getRadiusScaled, category)
  }

  /** Returns the object that is collided with the line */
  def collideLine(excludeObj: ObjectType, start: Vector2, end: Vector2,
              category: mutable.Buffer[GameElement]): Option[ObjectType] = {
    var coll = false
    var crashObj: Option[ObjectType] = None
    for ((owner, o) <- mapBufferIterator(units, category) if crashObj.isEmpty && o != excludeObj && o.collToMe) {
      coll = o.collBody.overlapsLine(start, end)
      if (coll) crashObj = Some(o)
    }

    crashObj
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


  /** Returns the filter that only includes map*/
  def mapFilter: mutable.Buffer[GameElement] = tmpMapBuffer

  /** Iterates every T of the
    *  mutable.LinkedHashMap[GameElement, mutable.Buffer[T]]
    *
    * @param mapBuffer A map which has a buffer in it.
    * @param catFilter CategoryFilter => If defined, iterator selects only one buffer from the map.
    * @return a new iterator.
    * */
  private def mapBufferIterator[T]
  (mapBuffer: mutable.Map[GameElement, mutable.Buffer[T]],
   catFilter: mutable.Buffer[GameElement]): Iterator[(GameElement, T)] =
    new Iterator[(GameElement, T)] {

      //iterates every category if the filter empty
      private val categoryI = if (catFilter.isEmpty) mapBuffer.iterator else filterIterator

      private var owner: GameElement = _ //will be given a value at first call of nextOwner
      private var bodyI: Iterator[T] = Iterator[T]()

      nextOwner()

      override def hasNext: Boolean = bodyI.hasNext || categoryI.hasNext

      override def next(): (GameElement, T) = {
        val nxt = (owner, bodyI.next())
        nextOwner() //hasNext has to know if a next category, with bodies, exists
        nxt
      }

      //selects next owner / category
      private def nextOwner(): Unit = {
        //finds the category which has elements
        while (!bodyI.hasNext && categoryI.hasNext) {
          val nxt = categoryI.next
          owner = nxt._1
          bodyI = nxt._2.iterator
        }

      }


      //iterates every selected category
      private lazy val tmpBuffer = mutable.Buffer.empty[T] //caches empty buffer
      private def filterIterator: Iterator[(GameElement, mutable.Buffer[T])] =
        new Iterator[(GameElement, mutable.Buffer[T])] {

          private val filt = catFilter.iterator

          override def hasNext: Boolean = filt.hasNext

          override def next(): (GameElement, mutable.Buffer[T]) = {
            val own = filt.next()
            val buff = mapBuffer.getOrElse(own, tmpBuffer)
            (own, buff)
          }
        }

    }

}
