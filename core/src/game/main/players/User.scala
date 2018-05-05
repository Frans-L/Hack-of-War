package game.main.players

import game.main.gameworld.gameobject.ObjectHandler
import game.main.unitcreators.units.{BuildingLane, BuildingMain}

class User(objectHandler: ObjectHandler, override val colorIndex: Int) extends
  Player(objectHandler, colorIndex) {

  initialize()

  override def initialize(): Unit = {
    super.initialize()

    //created the turrets
    spawnUnit(BuildingMain, 0, 0, objectHandler.collHandler.map.turretPath(0)) //main buildings
    spawnUnit(BuildingLane, 0, 0, objectHandler.collHandler.map.turretPath(1)) //lanes
    spawnUnit(BuildingLane, 0, 0, objectHandler.collHandler.map.turretPath(2))
  }

}
