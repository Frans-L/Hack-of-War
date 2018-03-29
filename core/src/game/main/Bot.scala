package game.main

import game.GameElement
import game.loader.GameTextures
import game.main.physics.PhysicsWorld

class Bot(textures: GameTextures, physWorld: PhysicsWorld, index: Int) extends
  Player(textures, physWorld, index) {


  override def initialize(): Unit = {
    spawnUnit(
      physWorld.map.dimensions.leftMiddle,
      physWorld.map.dimensions.upMiddle )
  }

}
