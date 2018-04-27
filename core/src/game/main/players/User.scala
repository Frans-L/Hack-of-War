package game.main.players

import game.main.cards.{Card, UnitCard}
import game.main.gameworld.collision.CollisionHandler
import game.main.gameworld.gameobject.ObjectHandler
import game.main.units.SoldierCreator

class User(objectHandler: ObjectHandler, override val colorIndex: Int) extends
  Player(objectHandler, colorIndex) {

  initialize()

  def initialize(): Unit = {
    hand.append(new UnitCard(this, SoldierCreator))
    hand.append(new UnitCard(this, SoldierCreator))
    hand.append(new UnitCard(this, SoldierCreator))
    hand.append(new UnitCard(this, SoldierCreator))
  }

}
