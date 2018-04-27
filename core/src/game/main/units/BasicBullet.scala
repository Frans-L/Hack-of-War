package game.main.units

import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.gameobject.elements.BulletCollision
import game.main.gameworld.gameobject.objects

object BasicBullet extends BulletCreator {

  override val texture: UnitTextures = GameTextures.Units.BaseBullet

  //size info
  private val scale = 1.0f
  override val radius: Float = 20f / 2f / scale

  /** Sets the all specific stats to the bullet. */
  override protected def setStats(obj: objects.BulletObject): Unit = {
    obj.appendElement(BulletCollision)
    obj.mass = 50f
  }
}
