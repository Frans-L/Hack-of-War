package game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.TimeUtils


object Ticker {

  //null value => if this is not set to be a Ticker, errors will appear
  var defaultTicker: Ticker = _

}

/**
  * Created by Frans on 26/02/2018.
  */
class Ticker(startTick: Long) {

  private var tick: Long = 0
  private var lastTick: Long = 0

  private var interval2s: Long = 0

  //Time since last update (ms)
  def delta: Int = (tick - lastTick).toInt

  //Time since the ticker started (ms)
  def total: Long = tick

  //Updates the time
  def update(currentTick: Long): Unit = {

    //one frame delay for the interval
    if (interval2s <= tick) interval2s = tick + 2000

    //update time
    lastTick = tick
    tick = currentTick - startTick
  }

  //Resets the total time
  def reset(currentTick: Long): Unit = {
    tick = currentTick
    lastTick = currentTick
  }


  //Returns true two every two seconds
  def interval2: Boolean = interval2s <= tick


}
