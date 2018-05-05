package game.main.unitcreators.bullet

import com.badlogic.gdx.math.Vector2
import game.GameElement
import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies
import game.main.gameworld.collision.bodies.CircleBody
import game.main.gameworld.gameobject.ObjectHandler
import game.main.gameworld.gameobject.ObjectHandler.Level
import game.main.gameworld.gameobject.objects.elements.{ShadowElement, TextureElement}
import game.main.gameworld.gameobject.objects.BulletObject

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

    bullet.appendElement(
      new ShadowElement(GameTextures.default.atlas.findRegion(texture.shadow)))
    bullet.appendElement(
      new TextureElement(
        GameTextures.default.atlas.findRegion(texture.main(colorIndex)), texture.brightness))

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
