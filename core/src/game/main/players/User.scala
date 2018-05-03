package game.main.players

import game.main.cards.{Card, UnitCard}
import game.main.gameworld.collision.CollisionHandler
import game.main.gameworld.gameobject.ObjectHandler
import game.main.units.{SoldierCreator, TurretCreator}

class User(objectHandler: ObjectHandler, override val colorIndex: Int) extends
  Player(objectHandler, colorIndex) {

  initialize()

  override def initialize(): Unit = {
    super.initialize()

    //created the turrets
    spawnUnit(TurretCreator, 0, 0, objectHandler.collHandler.map.turretPath(2))
    spawnUnit(TurretCreator, 0, 0, objectHandler.collHandler.map.turretPath(1))

    hand.append(new UnitCard(this, SoldierCreator))
    hand.append(new UnitCard(this, SoldierCreator))
    hand.append(new UnitCard(this, SoldierCreator))
    hand.append(new UnitCard(this, SoldierCreator))
  }

}
