package game.util

/** Super simple second countdown timer. */
class TimerSecond(var startTime: Float) {

  var time: Float = startTime

  /** Delta as milliseconds. */
  def update(delta: Int): Unit = {
    time = math.max(time - delta / 1000f, 0)
  }

  def isOver: Boolean = time == 0f

  override def toString: String = {
    val minutes = (time / 60).toInt
    val seconds = (time % 60).toInt
    "%01d:%02d".format(minutes, seconds)
  }

}
