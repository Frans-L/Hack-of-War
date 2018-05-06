package game.util

import com.badlogic.gdx.Gdx

/** A simple timer that can be reversed. */
class ProgressTimer(var duration: Int) {

  var running: Boolean = true
  var elapsed: Int = 0
  var direction: Int = 1

  def update(deltaTick: Int): Unit = {
    if (running) {
      elapsed += deltaTick * direction

      if (elapsed >= duration && direction > 0) {
        elapsed = duration
        running = false
      } else if (elapsed <= 0 && direction < 0) {
        elapsed = 0
        running = false
      }
    }
  }

  /** Returns true if progress is 100% */
  def isMax: Boolean = duration <= elapsed

  /** Returns true if progress is 0% */
  def isZero: Boolean = 0 >= elapsed

  def progress: Float = elapsed.toFloat / duration.toFloat

  /** Returns itself to make chaining possible */
  def reset(): ProgressTimer = {
    elapsed = if (direction > 0) 0 else duration
    this
  }

  def stop(): ProgressTimer = {
    running = false
    this
  }

  def start(): ProgressTimer = {
    running = true
    this
  }

  def setDuration(d: Int): ProgressTimer = {
    duration = d
    this
  }

  def reverse(): ProgressTimer = {
    direction = direction * -1
    this
  }

  def backward(): ProgressTimer = {
    direction = -1
    this
  }

  def forward(): ProgressTimer = {
    direction = 1
    this
  }

  /** Returns time in seconds.
    * Assuming that timer is used with milliseconds. */
  override def toString: String = {
    val minutes = (elapsed / 1000f / 60).toInt
    val seconds = (elapsed / 1000f % 60).toInt
    "%01d:%02d".format(minutes, seconds)
  }

}
