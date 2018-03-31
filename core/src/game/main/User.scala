package game.main

import game.loader.GameTextures
import game.main.physics.PhysicsWorld

class User(textures: GameTextures, physWorld: PhysicsWorld, index: Int) extends
  Player(textures, physWorld, index) {

  initialize()

  def initialize(): Unit = {
    hand.append(new Card(this))
    hand.append(new Card(this))
    hand.append(new Card(this))
    hand.append(new Card(this))
  }

}
