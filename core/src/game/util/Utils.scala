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
