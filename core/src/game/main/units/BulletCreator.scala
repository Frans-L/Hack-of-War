package game.main.units

import com.badlogic.gdx.math.Vector2
import game.GameElement
import game.loader.UnitTextures
import game.main.objects.BulletObject
import game.main.physics.PhysicsWorld
import game.main.physics.collision.{CircleBody, CollisionBody}
import game.main.units.BasicBullet.{radius, textures}
import game.util.Vector2e

trait BulletCreator {

  val textures: UnitTextures
  val radius: Float

  /** Sets the all specific stats to the bullet. */
  protected def setStats(obj: BulletObject): Unit

  /** Creates a new bullet and returns it. */
  def create(owner: GameElement, physWorld: PhysicsWorld,
             pos: Vector2, velocity: Vector2,
             colorIndex: Int): BulletObject = {

    val body: CollisionBody = new CircleBody(radius)

    val bullet: BulletObject = new BulletObject(
      textures, colorIndex,
      Vector2e(radius * 2f, radius * 2f),
      owner, physWorld, body,
      pos, velocity.scl(1f)
    )

    bullet.collToMe = false //make sure that others object doesn't collide with bullet
    setStats(bullet) //sets the specific stats
    bullet

  }


}
