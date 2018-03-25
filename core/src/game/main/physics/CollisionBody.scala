package game.main.physics

import com.badlogic.gdx.math.{Intersector, Polygon, Vector2}
import game.util.Vector2e

/**
  * Created by Frans on 14/03/2018.
  */
class CollisionBody(vertices: Array[Float], private var radius: Float)
  extends Polygon(vertices) {

  //caches the center point vector
  private val centerVector: Vector2 = Vector2e(0, 0)

  /**
    * Setters and getters to match with coding style of Polygon
    */
  def getRadius: Float = radius

  def getRadiusScaled: Float = radius * math.max(getScaleX, getScaleY)

  def setRadius(r: Float): Unit = radius = r

  def center: Vector2 = centerVector.set(getX + getOriginX, getY + getOriginY)

  def asPolygon: Polygon = this.asInstanceOf[Polygon]

  def asVertices: Array[Float] = getTransformedVertices


  /** Moves the body */
  def moveBy(v: Vector2): Unit = setPosition(getX + v.x, getY + v.y)

  def moveBy(v: Vector2, mult: Float): Unit = setPosition(getX + v.x * mult, getY + v.y * mult)


  /** Returns true if collided and angle of the collided polyline */
  def overlapsCircle(collBody: CollisionBody): (Boolean, Float) = {
    overlapsCircle(collBody.center, collBody.getRadiusScaled)
  }


  /**
    * Inspired by https://stackoverflow.com/questions/15323719/circle-and-polygon-collision-with-libgdx
    *
    * @param center center of the circle
    * @param r      radius
    * @return (isOverlapping, angle)
    */
  def overlapsCircle(center: Vector2, r: Float): (Boolean, Float) = {
    val r2: Float = r * r

    val verts: Array[Float] = getTransformedVertices

    val v1 = Vector2e.pool()
    val v2 = Vector2e.pool()

    var result = false
    var angle: Float = 0

    result = Intersector.intersectSegmentCircle(
      v1.set(verts(verts.length - 2), verts(verts.length - 1)),
      v2.set(verts(0), verts(1)), center, r2)

    if (result) angle = (math.atan2(
      verts(1) - verts(verts.length - 1),
      verts(0) - verts(verts.length - 2))
      * 180d / math.Pi).toFloat

    for (i <- 2 until verts.length by 2 if !result) {
      result = Intersector.intersectSegmentCircle(
        v1.set(verts(i - 2), verts(i - 1)),
        v2.set(verts(i), verts(i + 1)), center, r2)
      if (result) angle = (math.atan2(
        verts(i + 1) - verts(i - 1),
        verts(i) - verts(i - 2))
        * 180d / math.Pi).toFloat
    }

    (result || contains(center), angle)
  }

}
