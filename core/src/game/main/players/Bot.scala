package game.main.players

import com.badlogic.gdx.math.MathUtils
import game.main.physics.PhysicsWorld
import game.util.Ticker

class Bot(physWorld: PhysicsWorld, override val colorIndex: Int) extends
  Player(physWorld, colorIndex) {

  initialize()

  def initialize(): Unit = {
    spawnUnit(
      physWorld.dimensions.leftMiddle,
      physWorld.dimensions.upMiddle, physWorld.map.randomPathReversed)
  }

  override def update(): Unit = {
    super.update()

    if (Ticker.defaultTicker.interval10) {

      for (i <- 0 to MathUtils.random(3)) {
        spawnUnit(
          physWorld.dimensions.leftMiddle,
          physWorld.dimensions.upMiddle, physWorld.map.randomPathReversed, true)
      }

      for (i <- 0 to MathUtils.random(2)) {
        enemies.head.spawnUnit( //spawn to the player
          physWorld.dimensions.leftMiddle,
          physWorld.dimensions.upMiddle, physWorld.map.randomPath, true)
      }
    }

  }


}
