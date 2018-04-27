package game.util

import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import com.badlogic.gdx.math.{Intersector, Polygon, Rectangle, Vector2}
import game.main.gameworld.collision.bodies.PolygonBody

/**
  * Created by Frans on 11/03/2018.
  */
object Utils {

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

}
