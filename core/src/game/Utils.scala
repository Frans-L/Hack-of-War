package game

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
}
