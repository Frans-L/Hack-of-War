package game.main.units

import game.loader.{GameTextures, UnitTextures}
import game.main.objects.improved.objects.BulletObject

object BasicBullet extends BulletCreator {

  override val textures: UnitTextures = GameTextures.Units.BaseBullet

  //size info
  private val scale = 1.0f
  override val radius: Float = 20f / 2f / scale

  /** Sets the all specific stats to the bullet. */
  override protected def setStats(obj: BulletObject): Unit = Unit
}
