package game.main.players

import game.main.cards.{Card, UnitCard}
import game.main.objects.improved.ObjectHandler
import game.main.physics.PhysicsWorld
import game.main.units.SoldierCreator

class User(objectHandler: ObjectHandler, physWorld: PhysicsWorld, override val colorIndex: Int) extends
  Player(objectHandler, physWorld, colorIndex) {

  initialize()

  def initialize(): Unit = {
    hand.append(new UnitCard(this, SoldierCreator))
    hand.append(new UnitCard(this, SoldierCreator))
    hand.append(new UnitCard(this, SoldierCreator))
    hand.append(new UnitCard(this, SoldierCreator))
  }

}
