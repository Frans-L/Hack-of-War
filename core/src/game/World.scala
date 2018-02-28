package game

/**
  * Created by Frans on 26/02/2018.
  */
class World(val width: Int, val height: Int, val maxWidth: Int, val maxHeight: Int) {

  val left: Int = -width / 2 + 1
  val right: Int = width / 2
  val up: Int = height / 2
  val down: Int = -height / 2 + 1

  val maxLeft: Int = -maxWidth / 2 + 1
  val maxRight: Int = maxWidth / 2
  val maxUp: Int = maxHeight / 2
  val maxDown: Int = -maxHeight / 2 + 1

  val a: Long = 0x0000000000000000L

}
