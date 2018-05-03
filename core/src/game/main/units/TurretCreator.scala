package game.main.units

import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies.{CircleBody, CollisionBody}
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.objects.UnitObject

object TurretCreator extends UnitCreator {

  override lazy val cost: Int = 5
  override lazy val texture: UnitTextures = GameTextures.Units.BaseTurret
  override lazy val width: Float = 100f
  override lazy val height: Float = 100f
  override def collBody: CollisionBody = new CircleBody(width / 2f)

  /** Sets the all stats to to unit */
  override protected def setStats(obj: UnitObject, path: Path): Unit = {

    obj.mass = 100
    obj.static = true

  }
}
