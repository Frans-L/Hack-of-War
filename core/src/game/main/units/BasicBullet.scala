package game.main.units

import com.badlogic.gdx.math.Vector2
import game.GameElement
import game.loader.{GameTextures, UnitTextures}
import game.main.physics.PhysicsWorld
import game.main.physics.collision.{CircleBody, CollisionBody}
import game.main.objects.{BulletObject, UnitObject}
import game.util.{Utils, Vector2e}

object BasicBullet extends BulletCreator {

  override val textures: UnitTextures = GameTextures.Units.BaseBullet

  //size info
  private val scale = 1.0f
  override val radius: Float = 20f / 2f / scale

  /** Sets the all specific stats to the bullet. */
  override protected def setStats(obj: BulletObject): Unit = Unit
}
