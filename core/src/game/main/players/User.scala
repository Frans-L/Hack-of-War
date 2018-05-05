package game.main.players

import game.main.gameworld.gameobject.ObjectHandler
import game.main.units.{LaneBuilding, MainBuilding}

class User(objectHandler: ObjectHandler, override val colorIndex: Int) extends
  Player(objectHandler, colorIndex) {

  initialize()

  override def initialize(): Unit = {
    super.initialize()

    //created the turrets
    spawnUnit(MainBuilding, 0, 0, objectHandler.collHandler.map.turretPath(0)) //main buildings
    spawnUnit(LaneBuilding, 0, 0, objectHandler.collHandler.map.turretPath(1)) //lanes
    spawnUnit(LaneBuilding, 0, 0, objectHandler.collHandler.map.turretPath(2))
  }

}
