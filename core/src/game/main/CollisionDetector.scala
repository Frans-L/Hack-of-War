package game.main

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.{Intersector, Polygon, Vector2}

import scala.collection.mutable

/**
  * Created by Frans on 11/03/2♦018.
  *
  * Handles the physics and collision
  */
class CollisionDetector(val map: Map) {

  private val bodys: mutable.Buffer[CollisionBody] = mutable.Buffer[CollisionBody]()
  bodys ++= map.collPolygons //add map collissions


  //Returns true if collided
  def isCollided(obj: CollisionBody): Boolean = {
    var coll = false
    for (s <- bodys if !coll) {
      if (s != obj) {
        coll = Intersector.overlapConvexPolygons(obj, s)
      }
    }

    coll
  }

  //Returns true if collided
  def collide(obj: CollisionBody): Option[CollisionBody] = {
    var coll = false
    var body: Option[CollisionBody] = None
    for (b <- bodys if !coll && b != obj) {
      coll = Intersector.overlapConvexPolygons(obj, b)
      if (coll) body = Some(b)
    }
    body
  }

  //Returns the object that is collided with the point
  def collidePoint(obj: CollisionBody, point: Vector2): Option[CollisionBody] = {
    var coll = false
    var body: Option[CollisionBody] = None
    for (b <- bodys if !coll && b != obj) {
      coll = b.contains(point.x, point.y)
      if (coll) body = Some(b)
    }

    body
  }

  //Returns the collided object, and the angle of the polyline
  def collideAsCircle(obj: CollisionBody): (Option[CollisionBody], Float) = {
    var result: (Boolean, Float) = (false, 0f) //(is collided, angle)
    var body: Option[CollisionBody] = None
    for (b <- bodys if !result._1 && b != obj) {
      result = b.overlapsCircle(obj)
      if (result._1) body = Some(b)
    }
    (body, result._2)
  }

  def addShape(p: CollisionBody): Unit = bodys += p

  def removeShape(p: CollisionBody): Unit = bodys -= p

}
