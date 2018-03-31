package game.main

import game.GameElement
import game.loader.GameTextures
import game.main.physics.PhysicsWorld
import game.util.Ticker

class Bot(textures: GameTextures, physWorld: PhysicsWorld, index: Int) extends
  Player(textures, physWorld, index) {

  initialize()

  def initialize(): Unit = {
    spawnUnit(
      physWorld.map.dimensions.leftMiddle,
      physWorld.map.dimensions.upMiddle)
  }

  override def update(): Unit = {
    super.update()

    if(Ticker.defaultTicker.interval10){
      spawnUnit(
        physWorld.map.dimensions.leftMiddle,
        physWorld.map.dimensions.upMiddle)
    }

  }


}
