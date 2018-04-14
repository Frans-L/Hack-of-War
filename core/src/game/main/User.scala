package game.main

import game.loader.GameTextures
import game.main.physics.PhysicsWorld

class User(physWorld: PhysicsWorld, override val colorIndex: Int) extends
  Player(physWorld, colorIndex) {

  initialize()

  def initialize(): Unit = {
    hand.append(new Card(this))
    hand.append(new Card(this))
    hand.append(new Card(this))
    hand.append(new Card(this))
  }

}
