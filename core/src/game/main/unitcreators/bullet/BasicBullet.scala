package game.main.unitcreators.bullet

import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.gameobject.objects.elements.BulletCollision
import game.main.gameworld.gameobject.objects

object BasicBullet extends BulletCreator {

  override lazy val texture: UnitTextures = GameTextures.Units.BaseBullet

  //size info
  private lazy val scale = 1.0f
  override lazy val radius: Float = 20f / 2f / scale

  /** Sets the all specific stats to the bullet. */
  override protected def setStats(obj: objects.BulletObject): Unit = {
    val speed = 0.7f + obj.damage * 0.002f //speed will be relative to damage
    obj.velocity.scl(speed)

    obj.mass = 13f
    obj.appendElement(BulletCollision)
  }
}
