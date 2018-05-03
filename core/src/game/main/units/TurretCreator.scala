package game.main.units

import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies.{CircleBody, CollisionBody}
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.elements
import game.main.gameworld.gameobject.elements.{ShadowElement, SpriteElement}
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.players.Player

object TurretCreator extends UnitCreator {

  override lazy val cost: Int = 5
  override lazy val texture: UnitTextures = GameTextures.Units.BaseSoldier
  override lazy val width: Float = 100f
  override lazy val height: Float = 75f
  override def collBody: CollisionBody = new CircleBody(width / 2f)


  override protected def setSprite(obj: UnitObject, owner: Player): Unit = {
    super.setSprite(obj, owner)
  }


  /** Sets the all stats to to unit */
  override protected def setStats(obj: UnitObject, path: Path): Unit = {

    obj.mass = 100
    obj.health = 1000
    obj.static = true

  }
}
