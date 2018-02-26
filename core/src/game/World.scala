package game

/**
  * Created by Frans on 26/02/2018.
  */
class World(val width: Int, val height: Int, val maxWidth: Int, val maxHeight: Int) {

  val left = -width / 2 + 1
  val right = width / 2
  val up = height / 2
  val down = -height / 2 + 1

  val maxLeft: Int = -maxWidth / 2 + 1
  val maxRight = maxWidth / 2
  val maxUp = maxHeight / 2
  val maxDown = -maxHeight / 2 + 1

}
