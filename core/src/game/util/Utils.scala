package game.util

import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import com.badlogic.gdx.math.{Intersector, Polygon, Rectangle, Vector2}
import game.main.physics.collision.PolygonBody

/**
  * Created by Frans on 11/03/2018.
  */
object Utils {

  lazy val tmp1 = Vector2e(0, 0) //TODO: Does Java 7 support lazy val?
  lazy val tmp2 = Vector2e(0, 0)

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
  def triangleCollBody(x2: Float, y2: Float, x3: Float, y3: Float): PolygonBody = {
    val radius = (math.sqrt(math.max(x2 * x2 + y2 * y2, x3 * x3 + y3 * y3)) / 1.5f).toFloat
    val p: PolygonBody = new PolygonBody(Array(
      0, 0,
      x2, y2,
      x3, y3), radius)

    p
  }

  /** Normalizes a vector */
  def normalize(x: Float, y: Float): (Float, Float) = {
    val l = math.sqrt(x * x + y * y).toFloat
    (x / l, y / l)
  }


  /** Returns an angle between -90 and 450, that is closed to the target */
  def closestAngle360(angle: Float, target: Float): Float = {
    val a = absAngle(angle)
    val dist = ((target - a) / 180).toInt * 360
    a + dist
  }

  /** Returns angle between 0 - 360 */
  def absAngle(angle: Float): Float = ((angle % 360) + 360) % 360


  /** Returns true if the segment circle collided and sets the MinimumTranslationVector to
    * tell how much and in which direction the circle have to move to avoid collision.
    *
    * @return true if collided
    */
  def intersectSegmentCircle(start: Vector2, end: Vector2, center: Vector2, radius: Float,
                             mtv: MinimumTranslationVector): Boolean = {

    Intersector.nearestSegmentPoint(start, end, center, tmp1)
    tmp2.set(center)

    if (tmp2.dst2(tmp1) <= radius * radius) {
      mtv.depth = math.abs(tmp2.dst2(tmp1) - radius)
      mtv.normal = mtv.normal.set(tmp2.sub(tmp1).nor())
      true
    } else
      false

  }


}
