package game.main.units

import com.badlogic.gdx.graphics.g2d.Sprite
import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies.{CollisionBody, PolygonBody}
import game.main.gameworld.gameobject.elements
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.ObjectHandler.Level
import game.main.gameworld.gameobject.elements.{ShadowElement, SpriteElement}
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.players.Player
import game.util.Vector2e

object UnitCreator {

  /** Changes the sprite to be default pathIcon */
  def defaultPathIcon(sprite: Sprite): Sprite = {
    val darkness = 0.25f
    sprite.setColor(darkness, darkness, darkness, 0.15f)
    sprite
  }

  /** Changes the sprite to be default cardIcon */
  def defaultCardIcon(sprite: Sprite): Sprite = {
    sprite //icon is the same as the unit
  }
}

trait UnitCreator {

  val cost: Int

  val texture: UnitTextures
  val width: Float
  val height: Float

  def collBody: CollisionBody

  /** Sets the all stats to to unit */
  protected def setStats(obj: UnitObject, path: Path): Unit

  /** Sets the sprites to units */
  protected def setSprite(obj: UnitObject, owner: Player): Unit = {
    val sprite = GameTextures.default.atlas.createSprite(texture.main(owner.colorIndex))
    obj.appendElement(
      new ShadowElement(GameTextures.default.atlas.findRegion(texture.shadow), sprite))
    obj.appendElement(
      new SpriteElement(sprite, false))
  }


  /** Creates a new unit */
  def create(owner: Player, x: Float, y: Float,
             path: Path,
             random: Boolean = false): UnitObject = {

    val collHandler = owner.objectHandler.collHandler

    //creates the unit
    val obj = new UnitObject(collHandler, collBody)
    obj.owner = owner
    obj.size.set(width, height)
    obj.origin.set(obj.size.x / 2f, obj.size.y / 2f)

    //adds sprites
    setSprite(obj, owner)

    //adds the object to handlers
    owner.objectHandler.addObject(obj, Level.ground, owner = owner)

    //find the route
    val p: Path = path.copy
    if (random) p.setOffset(p.randomOffset) else p.setOffset(path.findOffset(x, y))
    obj.pos.set(p.head)
    obj.angle = p.direction(0).angle

    setStats(obj, p) //sets the specific unit stats

    obj.update()
    obj
  }

  /** Returns the icon of the unit */
  def pathIcon(owner: Player): Sprite = {
    val sprite = GameTextures.default.atlas.createSprite(texture.main(owner.colorIndex))
    sprite.setSize(width, height)
    UnitCreator.defaultPathIcon(sprite)
  }

  /** Returns the card icon of the unit */
  def cardIcon(owner: Player): Sprite = {
    val sprite = GameTextures.default.atlas.createSprite(texture.main(owner.colorIndex))
    sprite.setSize(width, height)
    UnitCreator.defaultCardIcon(sprite)
  }

}
