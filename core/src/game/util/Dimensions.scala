package game.util

/**
  * Created by Frans on 26/02/2018.
  */
class Dimensions(val width: Int, val height: Int, val maxWidth: Int, val maxHeight: Int) {

  lazy val left: Int = -width / 2
  lazy val right: Int = width / 2
  lazy val up: Int = height / 2
  lazy val down: Int = -height / 2 + 1

  lazy val maxLeft: Int = -maxWidth / 2
  lazy val maxRight: Int = maxWidth / 2
  lazy val maxUp: Int = maxHeight / 2
  lazy val maxDown: Int = -maxHeight / 2

  def leftMiddle: Int = left/2
  def rightMiddle: Int = right/2
  def downMiddle: Int = down/2
  def upMiddle: Int = up/2


  def isInside(x: Float, y: Float): Boolean =
    !(x < maxLeft || x > maxRight || y < maxDown || y > maxUp)

}
