package game.main

import com.badlogic.gdx.math.{Intersector, Polygon}
import scala.collection.mutable

/**
  * Created by Frans on 11/03/2♦018.
  *
  * Handles the physics and collision
  */
class CollisionDetector(val map: Map) {

  private val shapes: mutable.Buffer[Polygon] = mutable.Buffer[Polygon]()
  shapes ++= map.collPolygons //add map collissions

  def collide(obj: Polygon): Boolean = {

    var coll = false
    for (s <- shapes if !coll) {
      if (s != obj) {
        coll = Intersector.overlapConvexPolygons(obj, s)
      }
    }

    coll
  }

  def addShape(p: Polygon): Unit = shapes += p

  def removeShape(p: Polygon): Unit = shapes -= p

}
