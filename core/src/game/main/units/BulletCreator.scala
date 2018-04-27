package game.main.units

import com.badlogic.gdx.math.Vector2
import game.GameElement
import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies
import game.main.gameworld.collision.bodies.CircleBody
import game.main.gameworld.gameobject.ObjectHandler
import game.main.gameworld.gameobject.ObjectHandler.Level
import game.main.gameworld.gameobject.objects.BulletObject
import game.util.Vector2e

trait BulletCreator {

  val texture: UnitTextures
  val radius: Float

  /** Sets the all specific stats to the bullet. */
  protected def setStats(obj: BulletObject): Unit

  /** Creates a new bullet and returns it. */
  def create(owner: GameElement, objectHandler: ObjectHandler,
             pos: Vector2, velocity: Vector2,
             colorIndex: Int): BulletObject = {

    val body: bodies.CollisionBody = new CircleBody(radius)

    val bullet = new BulletObject(objectHandler.collHandler, body)
    val shadow = new game.main.gameworld.gameobject.elements.RelativeSpriteElement(
      GameTextures.defaultTextures.atlas.createSprite(texture.shadow), false,
      Vector2e(-3, -3), Vector2e(0.9f, 0.9f), 0)
    val sprite = new game.main.gameworld.gameobject.elements.SpriteElement(
      GameTextures.defaultTextures.atlas.createSprite(texture.main(colorIndex)), false)

    bullet.appendElement(shadow)
    bullet.appendElement(sprite)
    bullet.size.set(radius * 2, radius * 2)
    bullet.origin.set(radius, radius)
    bullet.pos.set(pos)
    bullet.velocity.set(velocity)
    bullet.friction = 0f
    objectHandler.addObject(bullet, Level.top, collision = false) //no general collision

    setStats(bullet) //sets the specific stats
    bullet.update()

    bullet
  }


}
