package game.main.units

import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.gameobject.elements.BulletCollision
import game.main.gameworld.gameobject.objects

object BasicBullet extends BulletCreator {

  override lazy val texture: UnitTextures = GameTextures.Units.BaseBullet

  //size info
  private lazy val scale = 1.0f
  override lazy val radius: Float = 20f / 2f / scale

  /** Sets the all specific stats to the bullet. */
  override protected def setStats(obj: objects.BulletObject): Unit = {
    obj.collided = true //makes sure that BulletCollision won't destroy itself immediately
    obj.appendElement(BulletCollision)
    obj.mass = 50f
  }
}
