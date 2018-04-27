package game.main.units

import com.badlogic.gdx.graphics.g2d.Sprite
import game.loader.{GameTextures, UnitTextures}
import game.main.gameMap.Path
import game.main.objects.improved.ObjectHandler.Level
import game.main.objects.improved.{PhysicsElement, RelativeSpriteElement, SpriteElement, UnitObject}
import game.main.physics.CollisionHandler
import game.main.physics.collision.PolygonBody
import game.main.players.Player
import game.util.Vector2e

trait UnitCreator {

  val cost: Int

  val texture: UnitTextures
  val width: Float
  val height: Float

  /** Sets the all stats to to unit */
  protected def setStats(obj: UnitObject, path: Path): Unit

  /** Creates a new unit */
  def create(owner: Player, x: Float, y: Float,
             path: Path,
             random: Boolean = false): UnitObject = {

    val collHandler = owner.objectHandler.collHandler
    val icon = pathIcon(owner)
    val body: PolygonBody = PolygonBody.triangleCollBody(width, height / 2f, 0, height)

    //creates the unit
    val obj = new UnitObject(collHandler, body)
    obj.owner = owner

    //adds the sprites
    val shadow = new RelativeSpriteElement(
      GameTextures.defaultTextures.atlas.createSprite(texture.shadow), false,
        Vector2e(-3, -4), Vector2e(1, 1), 0)
    val spriteE = new SpriteElement(
      GameTextures.defaultTextures.atlas.createSprite(texture.main(owner.colorIndex)), false)
    obj.appendElement(shadow)
    obj.appendElement(spriteE)

    val size = Vector2e(width, height)
    obj.size.set(size)
    obj.origin.set(size.x / 2f, size.y / 2f)

    //adds the object to handlers
    owner.objectHandler.addObject(obj, Level.ground, owner = owner)

    //find the route
    val p: Path = path.copy.setOffset(path.findOffset(x, y))
    if (random) p.setOffset(p.randomOffset)
    obj.pos.set(p.head)

    setStats(obj, p) //sets the specific unit stats
    obj.update()

    obj
  }

  /** Returns the icon of the unit */
  def pathIcon(owner: Player): Sprite = {
    val darkness = 0.25f
    val sprite = GameTextures.defaultTextures.atlas.createSprite(texture.main(owner.colorIndex))
    sprite.setColor(darkness, darkness, darkness, 0.15f)
    sprite.setSize(width, height)
    sprite
  }

  /** Returns the card icon of the unit */
  def cardIcon(owner: Player): Sprite = {
    val darkness = 0f
    val sprite = GameTextures.defaultTextures.atlas.createSprite(texture.main(owner.colorIndex))
    //sprite.setColor(darkness, darkness, darkness, 0.15f)
    sprite.setSize(width, height)
    sprite
  }

}
