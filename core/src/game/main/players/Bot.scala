package game.main.players

import com.badlogic.gdx.math.MathUtils
import game.main.cards.UnitCard
import game.main.gameworld.gameobject.ObjectHandler
import game.main.units.{BasicSoldier, LaneBuilding, MainBuilding}
import game.util.Ticker

class Bot(objectHandler: ObjectHandler, override val colorIndex: Int) extends
  Player(objectHandler, colorIndex) {

  initialize()

  override def initialize(): Unit = {
    super.initialize()

    //created the turrets
    spawnUnit(MainBuilding, 0, 0, objectHandler.collHandler.map.turretPathReversed(0)) //main buildings
    spawnUnit(LaneBuilding, 0, 0, objectHandler.collHandler.map.turretPathReversed(1)) //lanes
    spawnUnit(LaneBuilding, 0, 0, objectHandler.collHandler.map.turretPathReversed(2))

    spawnUnit(
      BasicSoldier,
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

      for (i <- 0 to MathUtils.random(2)) {
        val path = objectHandler.collHandler.map.randomPath
        enemies.head.randomCard[UnitCard]().foreach(card =>
          enemies.head.spawnUnit(card.unitCreator, path.head.x, path.head.y, path, true))
      }
    }

  }


}
