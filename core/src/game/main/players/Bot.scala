package game.main.players

import game.main.physics.PhysicsWorld
import game.util.Ticker

class Bot(physWorld: PhysicsWorld, override val colorIndex: Int) extends
  Player(physWorld, colorIndex) {

  initialize()

  def initialize(): Unit = {
    spawnUnit(
      physWorld.dimensions.leftMiddle,
      physWorld.dimensions.upMiddle)
  }

  override def update(): Unit = {
    super.update()

    if(Ticker.defaultTicker.interval10){
      spawnUnit(
        physWorld.dimensions.leftMiddle,
        physWorld.dimensions.upMiddle)
    }

  }


}
