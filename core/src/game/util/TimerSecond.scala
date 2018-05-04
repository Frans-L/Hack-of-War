package game.util

/** Super simple second countdown timer. */
class TimerSecond(var startTime: Float) {

  var time: Float = startTime

  /** Delta as milliseconds. */
  def update(delta: Int): Unit = {
    time -= delta / 1000f
  }

  override def toString: String = {
    val minutes = (time / 60).toInt
    val seconds = (time % 60).toInt
    "%01d:%02d".format(minutes, seconds)
  }

}
