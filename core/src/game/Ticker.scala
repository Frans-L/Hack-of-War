package game


/**
  * Created by Frans on 26/02/2018.
  */
class Ticker(startTick: Long) {

  private var tick: Long = 0
  private var lastTick: Long = 0

  //Time since last update (ms)
  def delta: Int = (tick - lastTick).toInt

  //Time since the ticker started (ms)
  def total: Long = tick

  def update(currentTick: Long): Unit = {
    lastTick = tick
    tick = currentTick - startTick
  }


}
