package game.util


object Ticker {

  //null value at start =>
  //errors will appear when game starts, if this not set to be something
  var defaultTicker: Ticker = _

}

/**
  * Created by Frans on 26/02/2018.
  */
class Ticker(startTick: Long) {

  var speed: Float = 1f

  private var tick: Long = 0
  private var lastTick: Long = 0

  //TODO: Hardcoded timers for quick debugging
  private var interval2s: Long = 0
  private var interval4s: Long = 0
  private var interval10s: Long = 0

  //time since last update (ms)
  def delta: Int = ((tick - lastTick) * speed).toInt

  //time since the ticker started (ms)
  def total: Long = tick

  /** Updates the time */
  def update(currentTick: Long): Unit = {

    //one frame delay for the interval
    if (interval2s <= tick) interval2s = tick + 2000
    if (interval4s <= tick) interval4s = tick + 4000
    if (interval10s <= tick) interval10s = tick + 10000

    //update time
    lastTick = tick
    tick = currentTick - startTick
  }

  /** Resets the total time */
  def reset(currentTick: Long): Unit = {
    tick = currentTick
    lastTick = currentTick
  }

  /** Returns true two every two seconds */
  def interval4: Boolean = interval4s <= tick

  /** Returns true two every two seconds */
  def interval2: Boolean = interval2s <= tick

  def interval10: Boolean = interval10s <= tick


}
