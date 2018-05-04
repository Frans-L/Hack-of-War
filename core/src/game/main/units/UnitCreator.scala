package game.main.units

import com.badlogic.gdx.graphics.g2d.Sprite
import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies.{CollisionBody, PolygonBody}
import game.main.gameworld.gameobject.elements
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.ObjectHandler.Level
import game.main.gameworld.gameobject.elements.unit.{HealthBarElement, UnitTextureElement}
import game.main.gameworld.gameobject.elements.{ShadowElement, TextureElement}
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.players.Player
import game.util.Vector2e



trait UnitCreator {

  /** Cost of the unit */
  val cost: Int

  /** Creates a new unit */
  def create(owner: Player,
             x: Float, y: Float,
             path: Path, random: Boolean = false): UnitObject

  /** Returns the icon of the unit */
  def pathIcon(owner: Player): Sprite

  /** Returns the card icon of the unit */
  def cardIcon(owner: Player): Sprite


}

/** Helper methods to create units easily. */
object UnitCreator {

  /** Sets the sprites to units */
  def addTextures(obj: UnitObject, texture: UnitTextures, owner: Player): Unit = {
    obj.appendElement(
      new ShadowElement(GameTextures.default.atlas.findRegion(texture.shadow)))
    obj.appendElement(
      new UnitTextureElement(
        GameTextures.default.atlas.findRegion(texture.main(owner.colorIndex)), texture.brightness))
  }

  /** Creates the empty unitObject */
  def createUnit(owner: Player, collBody: CollisionBody,
                           width: Float, height: Float,
                           update: Boolean = true): UnitObject = {

    val obj = new UnitObject(owner.objectHandler.collHandler, collBody)
    obj.owner = owner
    obj.size.set(width, height)
    obj.origin.set(obj.size.x / 2f, obj.size.y / 2f)
    if (update) owner.objectHandler.addObject(obj, Level.ground, owner = owner)
    obj.update()
    obj

  }

  /** Adds a moving path to object. + Moves the object to the beginning of the path. */
  def findPath(obj: UnitObject, path: Path,
                         x: Float, y: Float,
                         random: Boolean = false): Path = {

    val p: Path = path.copy
    if (random) p.setOffset(p.randomOffset) else p.setOffset(path.findOffset(x, y))
    posToStartLocRandom(obj, path)
    p

  }

  /** Moves the object to the beginning of the path. */
  def posToStartLoc(obj: UnitObject, path: Path): Unit = {
    obj.pos.set(path.head)
    obj.angle = path.direction(0).angle
  }

  /** Moves the object to the beginning of the path between 1st and 2nd point. */
  def posToStartLocRandom(obj: UnitObject, path: Path): Unit = {
    obj.pos.set(path.randomSpot(0))
    obj.angle = path.direction(1).angle
  }


  /** Returns the default path icon. */
  def defaultPathIcon(texture: UnitTextures,
                                width: Float, height: Float,
                                owner: Player): Sprite = {
    val darkness = 0.25f
    val sprite = GameTextures.default.atlas.createSprite(texture.main(owner.colorIndex))
    sprite.setSize(width, height)
    sprite.setColor(darkness, darkness, darkness, 0.15f)
    sprite
  }

  /** Returns the default card icon. */
  def defaultCardIcon(texture: UnitTextures,
                                width: Float, height: Float,
                                owner: Player): Sprite = {
    val sprite = GameTextures.default.atlas.createSprite(texture.main(owner.colorIndex))
    sprite.setColor(texture.brightness, texture.brightness, texture.brightness, 1)
    sprite.setSize(width, height)
    sprite
  }

}
