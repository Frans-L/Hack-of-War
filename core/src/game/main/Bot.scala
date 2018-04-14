package game.main

import game.GameElement
import game.loader.GameTextures
import game.main.physics.PhysicsWorld
import game.util.Ticker

class Bot(physWorld: PhysicsWorld, override val colorIndex: Int) extends
  Player(physWorld, colorIndex) {

  initialize()

  def initialize(): Unit = {
    spawnUnit(
      physWorld.map.dimensions.leftMiddle,
      physWorld.map.dimensions.upMiddle)
  }

  override def update(): Unit = {
    super.update()

    if(false && Ticker.defaultTicker.interval10){
      spawnUnit(
        physWorld.map.dimensions.leftMiddle,
        physWorld.map.dimensions.upMiddle)
    }

  }


}
