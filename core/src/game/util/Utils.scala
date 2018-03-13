package game.util

import com.badlogic.gdx.math.{Polygon, Rectangle}

/**
  * Created by Frans on 11/03/2018.
  */
object Utils {


  //creates polygon that is shaped rectangle
  def rectanglePolygon(x: Float, y: Float, w: Float, h: Float): Polygon = {
    val p: Polygon = new Polygon(Array(
      0, 0,
      w, 0,
      w, h,
      0, h))

    p.setPosition(x, y)
    p
  }

  //creates polygon that is shaped rectangle, the first point is the origin
  def trianglePolygon(x2: Float, y2: Float, x3: Float, y3: Float): Polygon = {
    val p: Polygon = new Polygon(Array(
      0, 0,
      x2, y2,
      x3, y3))

    p
  }

  //converts rectangle to polygon
  def rectangleToPolygon(r: Rectangle): Polygon = {
    val p: Polygon = new Polygon(Array(
      0, 0,
      r.width, 0,
      r.width, r.height,
      0, r.height))

    p.setPosition(r.x, r.y)
    p
  }

  def normalize(x: Float, y: Float): (Float, Float) = {
    val l = math.sqrt(x * x + y * y).toFloat
    (x / l, y / l)
  }

  def truncate(x: Float, y: Float, max: Float) = {

  }
}
