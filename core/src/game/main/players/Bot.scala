package game.main.players

import com.badlogic.gdx.math.MathUtils
import game.main.objects.improved.ObjectHandler
import game.main.physics.PhysicsWorld
import game.main.units.SoldierCreator
import game.util.Ticker

class Bot(objectHandler: ObjectHandler, physWorld: PhysicsWorld, override val colorIndex: Int) extends
  Player(objectHandler, physWorld, colorIndex) {

  initialize()

  def initialize(): Unit = {
    spawnUnit(
      SoldierCreator,
      physWorld.dimensions.leftMiddle,
      physWorld.dimensions.upMiddle, physWorld.map.randomPathReversed)
  }

  override def update(): Unit = {
    super.update()

    if (Ticker.defaultTicker.interval10) {

      for (i <- 0 to MathUtils.random(3)) {
        spawnUnit(
          SoldierCreator,
          physWorld.dimensions.leftMiddle,
          physWorld.dimensions.upMiddle, physWorld.map.randomPathReversed, true)
      }

      for (i <- 0 to MathUtils.random(2)) {
        enemies.head.spawnUnit(
          SoldierCreator, //spawn to the player
          physWorld.dimensions.leftMiddle,
          physWorld.dimensions.upMiddle, physWorld.map.randomPath, true)
      }
    }

  }


}
