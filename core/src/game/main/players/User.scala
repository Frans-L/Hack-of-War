package game.main.players

import game.main.Card
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
