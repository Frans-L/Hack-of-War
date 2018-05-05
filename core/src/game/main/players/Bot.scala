package game.main.players

import com.badlogic.gdx.math.MathUtils
import game.main.cards.UnitCard
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.ObjectHandler
import game.main.unitcreators.units.{BuildingLane, BuildingMain, SoldierBasic}
import game.util.Ticker

class Bot(objectHandler: ObjectHandler, override val colorIndex: Int) extends
  Player(objectHandler, colorIndex) {

  override protected val turretPaths: Seq[Path] = objectHandler.collHandler.map.turretPathReversed
  override protected val paths: Seq[Path] = objectHandler.collHandler.map.pathReversed

  initialize()

  override def initialize(): Unit = {
    super.initialize()

    spawnUnit(
      SoldierBasic,
      objectHandler.collHandler.dimensions.leftMiddle,
      objectHandler.collHandler.dimensions.upMiddle, objectHandler.collHandler.map.randomPathReversed)
  }

  override def update(): Unit = {
    super.update()

    if (Ticker.defaultTicker.interval10) {

      for (i <- 0 to MathUtils.random(3)) {
        val path = objectHandler.collHandler.map.randomPathReversed
        randomCard[UnitCard]().foreach(card =>
          spawnUnit(card.unitCreator, path.head.x, path.head.y, path, true))
      }

      /*
      for (i <- 0 to MathUtils.random(2)) {
        val path = objectHandler.collHandler.map.randomPath
        enemies.head.randomCard[UnitCard]().foreach(card =>
          enemies.head.spawnUnit(card.unitCreator, path.head.x, path.head.y, path, true))
      }
      */

    }

  }


}
