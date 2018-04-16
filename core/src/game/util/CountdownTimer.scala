package game.util

/** A simple countdown timer that can be reversed. */
class CountdownTimer(var duration: Int) {

  var running: Boolean = true
  var elapsed: Int = 0
  var direction: Int = 1

  def update(deltaTick: Int): Unit = {
    if (running) {
      elapsed += deltaTick * direction

      if (elapsed >= duration) {
        elapsed = duration
        running = false
      } else if (elapsed <= 0) {
        elapsed = 0
        running = false
      }
    }
  }

  def isEnd: Boolean = duration <= elapsed

  def isEndReversed: Boolean = 0 >= elapsed

  def isOn: Boolean = !isEnd

  def isOnReversed: Boolean = !isEndReversed

  def progress: Float = elapsed.toFloat / duration.toFloat

  /** Returns itself to make chaining possible */
  def reset(): CountdownTimer = {
    elapsed = if (direction > 0) 0 else duration
    this
  }

  def stop(): CountdownTimer = {
    running = false
    this
  }

  def start(): CountdownTimer = {
    running = true
    this
  }

  def setDuration(d: Int): CountdownTimer = {
    duration = d
    this
  }

  def reverse(): CountdownTimer = {
    direction = direction * -1
    this
  }

  def backward(): CountdownTimer = {
    direction = -1
    this
  }

  def forward(): CountdownTimer = {
    direction = 1
    this
  }

}
