package game.main.physics.collision

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import com.badlogic.gdx.math.{Intersector, Polygon, Vector2}
import com.badlogic.gdx.utils.{GdxRuntimeException, Pool, Pools}
import game.util.{Utils, Vector2e}


object PolygonBody {

  /** Creates CollisionBody that is shaped trapezoid. */
  def trapezoidCollBody(leftHeight: Float, rightHeight: Float, length: Float): PolygonBody = {
    val radius = math.max(math.max(leftHeight, rightHeight), length) / 2f
    val middle = leftHeight / 2f
    val p: PolygonBody = new PolygonBody(Array(
      0, 0,
      length, middle - rightHeight / 2f,
      length, middle + rightHeight / 2f,
      0, leftHeight), radius)

    p
  }

  /** Creates CollisionBody that is shaped rectangle */
  def rectangleCollBody(x: Float, y: Float, w: Float, h: Float): PolygonBody = {
    val radius = math.max(w, h) / 2
    val p: PolygonBody = new PolygonBody(Array(
      0, 0,
      w, 0,
      w, h,
      0, h), radius)

    p.setPosition(x, y)
    p.setOrigin(w / 2f, h / 2f)

    p
  }

  /** Creates CollisionBody that is shaped rectangle, the first point is the origin */
  def triangleCollBody(x2: Float, y2: Float, x3: Float, y3: Float): PolygonBody =
    triangleCollBody(0, 0, x2, y2, x3, y3)

  /** Creates CollisionBody that is shaped rectangle, the first point is the origin */
  def triangleCollBody(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float): PolygonBody = {
    val radius = (math.sqrt(math.max(x2 * x2 + y2 * y2, x3 * x3 + y3 * y3)) / 1.5f).toFloat
    val p: PolygonBody = new PolygonBody(Array(
      0, 0,
      x2, y2,
      x3, y3), radius)

    p.setPosition(x1, y1)
    p
  }

}

/**
  * Created by Frans on 14/03/2018.
  * Polygon with radius variable wrapped into CollisionBody
  */
class PolygonBody(vertices: Array[Float], private var radius: Float) extends
  Polygon(vertices) with CollisionBody {

  /**
    * Setters and getters to match with coding style of Polygon
    */
  def getRadius: Float = radius

  def getRadiusScaled: Float = radius * math.max(getScaleX, getScaleY)

  def setRadius(r: Float): Unit = radius = r

  def asPolygon: Polygon = this.asInstanceOf[Polygon]

  def asVertices: Array[Float] = getTransformedVertices


  /**
    * Inspired by https://stackoverflow.com/questions/15323719/circle-and-polygon-collision-with-libgdx
    *
    * @param center center of the circle
    * @param r      radius
    * @return (isOverlapping, angle)
    */
  override def overlapsCircle(center: Vector2,
                              r: Float,
                              mtv: MinimumTranslationVector): Boolean = {

    val verts: Array[Float] = getTransformedVertices
    val v1 = Vector2e.pool()
    val v2 = Vector2e.pool()

    //if the center of the circle is in inside of the polygon
    var result = this.contains(center)

    if (!result) { //circle is outside of the polygon

      //checks if circle's edge collides with polygon' edges
      result = CollisionBody.intersectSegmentCircle(v1.set(verts(verts.length - 2), verts(verts.length - 1)),
        v2.set(verts(0), verts(1)),
        center, r,
        mtv)

      for (i <- 2 until verts.length by 2 if !result) {
        result = CollisionBody.intersectSegmentCircle(
          v1.set(verts(i - 2), verts(i - 1)),
          v2.set(verts(i), verts(i + 1)),
          center, r,
          mtv)
      }

    } else if (mtv != null) { //the center is inside of the polygon

      //calculates the mtv by finding the distance from the center to polygon's edge
      val v3 = Vector2e.pool()
      var dist2: Float = 0

      Intersector.nearestSegmentPoint(
        v1.set(verts(verts.length - 2), verts(verts.length - 1)),
        v2.set(verts(0), verts(1)), center, v3)
      v3.sub(center)

      dist2 = v3.len2()
      mtv.normal.set(v3).scl(-1) //use mtv as tmp vector

      for (i <- 2 until verts.length by 2) {
        Intersector.nearestSegmentPoint(
          v1.set(verts(i - 2), verts(i - 1)),
          v2.set(verts(i), verts(i + 1)),
          center, v3)
        v3.sub(center)

        val l = v3.len2()
        if (l < dist2) {
          dist2 = l
          mtv.normal.set(v3).scl(-1)
        }
      }

      mtv.depth = math.sqrt(dist2).toFloat + r
      mtv.normal.nor()
      Vector2e.free(v3)
    }


    Vector2e.free(v1) //frees the memory
    Vector2e.free(v2)

    result
  }

  /** Returns true if overlaps a line. */
  override def overlapsLine(startPos: Vector2, endPos: Vector2): Boolean = {
    Intersector.intersectSegmentPolygon(startPos, endPos, this)
  }

  /** Returns the Center vector and the radius */
  override def toCircle: (Vector2, Float) = ???

  /** Returns true if overlaps, and sets the mtv vector. */
  override def overlapsPolygon(polygon: PolygonBody, mtv: Intersector.MinimumTranslationVector): Boolean = {
    gdxfixed.math.Intersector.overlapConvexPolygons(this.asPolygon, polygon.asPolygon, mtv)
  }


  override def draw(shapeRender: ShapeRenderer): Unit = {
    shapeRender.polygon(getTransformedVertices)
    shapeRender.circle(center.x, center.y, 4)
  }

}
