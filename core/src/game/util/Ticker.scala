package game.util


object Ticker {

  //null value at start =>
  //errors will appear when game starts, if this not set to be something
  var defaultTicker: Ticker = new Ticker(0)

}

/**
  * Created by Frans on 26/02/2018.
  */
class Ticker(private var startTick: Long) {

  var speed: Float = 1f
  private var running: Boolean = true

  private var tick: Long = 0
  private var lastTick: Long = 0

  //TODO: Hardcoded timers for quick debugging
  private var interval2s: Long = 2000
  private var interval4s: Long = 4000
  private var interval10s: Long = 10000

  //time since last update (ms)
  def delta: Int = ((tick - lastTick) * speed).toInt
  4000
  //time since the ticker started (ms)
  def total: Long = tick

  /** Updates the time */
  def update(currentTick: Long): Unit = {
    if(running){
      //one frame delay for the interval
      if (interval2s <= tick) interval2s = tick + 2000
      if (interval4s <= tick) interval4s = tick + 4000
      if (interval10s <= tick) interval10s = tick + 10000

      //update time
      lastTick = tick
      tick = currentTick - startTick
    }
  }

  /** Resets the total time */
  def reset(currentTick: Long): Ticker = {
    startTick = currentTick
    tick = 0
    lastTick = 0
    this
  }

  /** Sets timer on. Returns itself for the chaining.*/
  def run(): Ticker = {
    running = true
    this
  }

  def stop(): Ticker = {
    running = false
    this
  }

  /** Returns true two every two seconds */
  def interval4: Boolean = interval4s <= tick

  /** Returns true two every two seconds */
  def interval2: Boolean = interval2s <= tick

  def interval10: Boolean = interval10s <= tick


}
