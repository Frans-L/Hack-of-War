package game.main.players

import game.main.cards.{Card, UnitCard}
import game.main.physics.PhysicsWorld

class User(physWorld: PhysicsWorld, override val colorIndex: Int) extends
  Player(physWorld, colorIndex) {

  initialize()

  def initialize(): Unit = {
    hand.append(new UnitCard(this))
    hand.append(new UnitCard(this))
    hand.append(new UnitCard(this))
    hand.append(new UnitCard(this))
  }

}
