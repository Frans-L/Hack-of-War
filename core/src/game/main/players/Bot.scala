package game.main.players

import com.badlogic.gdx.math.MathUtils
import game.main.gameworld.collision.CollisionHandler
import game.main.gameworld.gameobject.ObjectHandler
import game.main.units.SoldierCreator
import game.util.Ticker

class Bot(objectHandler: ObjectHandler, override val colorIndex: Int) extends
  Player(objectHandler, colorIndex) {

  initialize()

  def initialize(): Unit = {
    spawnUnit(
      SoldierCreator,
      objectHandler.collHandler.dimensions.leftMiddle,
      objectHandler.collHandler.dimensions.upMiddle, objectHandler.collHandler.map.randomPathReversed)
  }

  override def update(): Unit = {
    super.update()

    if (Ticker.defaultTicker.interval10) {

      for (i <- 0 to MathUtils.random(3)) {
        spawnUnit(
          SoldierCreator,
          objectHandler.collHandler.dimensions.leftMiddle,
          objectHandler.collHandler.dimensions.upMiddle,
          objectHandler.collHandler.map.randomPathReversed, true)
      }

      for (i <- 0 to MathUtils.random(2)) {
        enemies.head.spawnUnit(
          SoldierCreator, //spawn to the player
          objectHandler.collHandler.dimensions.leftMiddle,
          objectHandler.collHandler.dimensions.upMiddle,
          objectHandler.collHandler.map.randomPath, true)
      }
    }

  }


}
