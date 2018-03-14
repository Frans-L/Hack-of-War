package game.util

import com.badlogic.gdx.math.{Polygon, Rectangle}
import game.main.CollisionBody

/**
  * Created by Frans on 11/03/2018.
  */
object Utils {

  //Creates CollisionBody that is shaped rectangle
  def rectangleCollBody(x: Float, y: Float, w: Float, h: Float): CollisionBody = {
    val radius = math.max(w, h) / 2
    val p: CollisionBody = new CollisionBody(Array(
      0, 0,
      w, 0,
      w, h,
      0, h), radius)

    p.setPosition(x, y)
    p.setOrigin(w/2f, h/2f)

    p
  }

  //Creates CollisionBody that is shaped rectangle, the first point is the origin
  def triangleCollBody(x2: Float, y2: Float, x3: Float, y3: Float): CollisionBody = {
    val radius = (math.sqrt(math.max(x2 * x2 + y2 * y2, x3 * x3 + y3 * y3)) / 1.5f).toFloat
    val p: CollisionBody = new CollisionBody(Array(
      0, 0,
      x2, y2,
      x3, y3), radius)

    p
  }

  def normalize(x: Float, y: Float): (Float, Float) = {
    val l = math.sqrt(x * x + y * y).toFloat
    (x / l, y / l)
  }


}
