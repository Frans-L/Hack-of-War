package game.main.physics

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import com.badlogic.gdx.math.{Intersector, Vector2}
import game.GameElement
import game.main.Player
import game.main.physics.objects.CollisionObject

import scala.collection.mutable


/**
  * Created by Frans on 11/03/2♦018.
  *
  * Handles the physics and collision
  */
class CollisionHandler(val map: Map) extends GameElement {


  // For instance --> bodys(player1)(0), bodys(map)(0)
  private val bodys: mutable.Map[GameElement, mutable.Buffer[CollisionBody]] =
    mutable.Map[GameElement, mutable.Buffer[CollisionBody]]()

  private val body: mutable.Buffer[CollisionBody] = mutable.Buffer[CollisionBody]()

  bodys.put(map, map.collPolygons) //add map collisions


  /** Checks if the obj is colliding with something.
    *
    * @param obj the collisionBody of the object
    * @param mtv sets the minimumTranslationVector that is required to separate objects
    * @return the body of the collided object
    */
  def collide(obj: CollisionBody, mtv: MinimumTranslationVector = null): Option[CollisionBody] = {
    var body: Option[CollisionBody] = None
    for (category <- bodys if body.isEmpty) {
      for (s <- category._2 if body.isEmpty && s != obj) {
        if (Intersector.overlapConvexPolygons(obj, s, mtv))
          body = Some(s)
      }
    }
    body
  }

  /** Return true if collided */
  def isCollided(obj: CollisionBody, mtv: MinimumTranslationVector = null): Boolean = {
    //collide(obj, mtv).isDefined
    var body: Option[CollisionBody] = None
    for((c, o) <- bodysIterator(bodys)){
      if(o != obj){
        if (Intersector.overlapConvexPolygons(obj, o, mtv))
          body = Some(o)
      }
    }
    body.isDefined
  }


  //Returns the object that is collided with the point
  def collidePoint(obj: CollisionBody, point: Vector2): Option[CollisionBody] = {
    var coll = false
    var body: Option[CollisionBody] = None
    for (category <- bodys if body.isEmpty) {
      for (b <- category._2 if body.isEmpty && b != obj) {
        coll = b.contains(point.x, point.y)
        if (coll) body = Some(b)
      }
    }

    body
  }

  //Returns the collided object, and the angle of the polyline
  def collideAsCircle(obj: CollisionBody, center: Vector2, radius: Float):
  (Option[CollisionBody], Float) = {

    var result: (Boolean, Float) = (false, 0f) //(is collided, angle)
    var body: Option[CollisionBody] = None
    for (category <- bodys if body.isEmpty) {
      for (b <- category._2 if !result._1 && b != obj) {
        result = b.overlapsCircle(center, radius)
        if (result._1) body = Some(b)
      }
    }

    (body, result._2)
  }

  def collideAsCircle(obj: CollisionBody): (Option[CollisionBody], Float) = {
    collideAsCircle(obj, obj.center, obj.getRadius)
  }

  def addShape(category: GameElement, p: CollisionBody): Unit = {
    if (bodys.contains(category))
      bodys(category) += p
    else
      bodys.put(category, mutable.Buffer[CollisionBody](p))
  }

  def removeShape(category: GameElement, p: CollisionBody): Unit = bodys(category) -= p

  override def update(): Unit = ???

  override def draw(shapeRender: ShapeRenderer): Unit = ???

  override def draw(batch: Batch): Unit = ???


  def bodysIterator
  (b: mutable.Map[GameElement, mutable.Buffer[CollisionBody]]): Iterator[(GameElement, CollisionBody)] =
    new Iterator[(GameElement, CollisionBody)] {

      private val categoryI = b.iterator
      private var category: GameElement = _
      private var bodyI: Iterator[CollisionBody] = Iterator[CollisionBody]()

      override def hasNext: Boolean = bodyI.hasNext || categoryI.hasNext

      override def next(): (GameElement, CollisionBody) = {
        if (!bodyI.hasNext) {
          val nxt = categoryI.next
          category = nxt._1
          bodyI = nxt._2.iterator
        }

        (category, bodyI.next())
      }
    }
}
