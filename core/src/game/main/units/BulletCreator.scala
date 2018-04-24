package game.main.units

import com.badlogic.gdx.math.Vector2
import game.GameElement
import game.loader.{GameTextures, UnitTextures}
import game.main.objects.improved.ObjectHandler.Level
import game.main.objects.improved.objects.BulletObject
import game.main.objects.improved.{ObjectHandler, PhysicsObject, SpriteElement}
import game.main.physics.CollisionHandler
import game.main.physics.collision.{CircleBody, CollisionBody}
import game.main.units.BasicBullet.{radius, textures}
import game.util.Vector2e

trait BulletCreator {

  val textures: UnitTextures
  val radius: Float

  /** Sets the all specific stats to the bullet. */
  protected def setStats(obj: BulletObject): Unit

  /** Creates a new bullet and returns it. */
  def create(owner: GameElement, objectHandler: ObjectHandler,
             pos: Vector2, velocity: Vector2,
             colorIndex: Int): BulletObject = {

    val body: CollisionBody = new CircleBody(radius)

    val bullet = new BulletObject(objectHandler.collHandler, body)
    val sprite = new SpriteElement(
      GameTextures.defaultTextures.atlas.createSprite(textures.main(colorIndex)), false)

    bullet.appendElement(sprite)
    bullet.size.set(radius * 2, radius * 2)
    bullet.pos.set(pos)
    bullet.velocity.set(velocity)
    bullet.friction = 0f
    objectHandler.addObject(bullet, Level.top, collision = false) //no general collision

    setStats(bullet) //sets the specific stats
    bullet.update()

    bullet
  }


}
