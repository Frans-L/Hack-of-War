package game.main.physics.collision

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import com.badlogic.gdx.math.{Intersector, Polygon, Vector2}
import com.badlogic.gdx.utils.GdxRuntimeException
import game.util.{Utils, Vector2e}

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

    var result = false

    result = Utils.intersectSegmentCircle(v1.set(verts(verts.length - 2), verts(verts.length - 1)),
      v2.set(verts(0), verts(1)),
      center, r,
      mtv)

    for (i <- 2 until verts.length by 2 if !result) {
      result = Utils.intersectSegmentCircle(
        v1.set(verts(i - 2), verts(i - 1)),
        v2.set(verts(i), verts(i + 1)),
        center, r,
        mtv)
    }

    Vector2e.free(v1) //frees the memory
    Vector2e.free(v2)

    result
  }

  /** Returns the Center vector and the radius */
  override def toCircle: (Vector2, Float) = ???

  /** Returns true if overlaps, and sets the mtv vector. */
  override def overlapsPolygon(polygon: PolygonBody, mtv: Intersector.MinimumTranslationVector): Boolean = {
    Intersector.overlapConvexPolygons(this.asPolygon, polygon.asPolygon, mtv)
  }

}
