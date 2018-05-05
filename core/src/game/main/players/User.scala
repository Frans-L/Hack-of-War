package game.main.players

import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.ObjectHandler
import game.main.unitcreators.units.{BuildingLane, BuildingMain}

class User(objectHandler: ObjectHandler, override val colorIndex: Int) extends
  Player(objectHandler, colorIndex) {

  override protected val turretPaths: Seq[Path] = objectHandler.collHandler.map.turretPath
  override protected val paths: Seq[Path] = objectHandler.collHandler.map.path

  initialize()

  override def initialize(): Unit = {
    super.initialize()
  }

}
