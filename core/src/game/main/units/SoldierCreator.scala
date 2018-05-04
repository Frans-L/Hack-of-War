package game.main.units

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies.{CollisionBody, PolygonBody}
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.elements.unit.HealthBarElement
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.players.Player
import game.main.units.UnitCreator

trait SoldierCreator extends UnitCreator {

  protected val texture: UnitTextures
  protected val width: Float
  protected val height: Float

  protected def collBody: CollisionBody

  protected def setStats(obj: UnitObject, owner: Player, path: Path): Unit

  /** Sets the all stats to to unit */
  override def create(owner: Player,
                      x: Float, y: Float,
                      path: Path, random: Boolean): UnitObject = {

    val obj = UnitCreator.createUnit(owner, collBody, width, height)
    UnitCreator.addTextures(obj, texture, owner)
    val correctPath = UnitCreator.findPath(obj, path, x, y, random)

    setStats(obj, owner, correctPath) //sets all the stats

    obj.appendElement(new HealthBarElement(obj.health)) //add healthBar
    obj.update()
    obj
  }

  /** Returns the icon of the unit */
  override def pathIcon(owner: Player): Sprite = {
    UnitCreator.defaultPathIcon(texture, width, height, owner)
  }

  /** Returns the card icon of the unit */
  override def cardIcon(owner: Player): Sprite = {
    UnitCreator.defaultCardIcon(texture, width, height, owner)
  }
}
