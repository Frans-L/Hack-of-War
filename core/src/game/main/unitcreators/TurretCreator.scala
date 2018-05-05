package game.main.unitcreators

import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies.{CircleBody, PolygonBody}
import game.main.gameworld.gameobject.GameObject
import game.main.gameworld.gameobject.objects.elements.unit.ai.{ShootAhead, TurnToTarget}
import game.main.gameworld.gameobject.objects.elements.{IconTextureElement, TextureElement, unit}
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.players.Player
import game.main.unitcreators.bullet.BasicBullet

trait TurretCreator {

  protected val texture: UnitTextures
  protected val width: Float
  protected val height: Float

  def setStats(turret: UnitObject, baseObj: UnitObject): Unit

  def create(owner: Player, baseObj: UnitObject): UnitObject = {

    val collBody = new CircleBody(2)
    val turret = UnitCreator.createUnit(owner, collBody, width, height, update = false)

    turret.opacity = 0.75f
    turret.angle = 0
    turret.origin.set(turret.size.x / 3f, turret.size.y / 2f)

    //add textures
    turret.appendElement(
      new TextureElement(GameTextures.default.atlas.findRegion(texture.main(baseObj.owner.colorIndex))))

    setStats(turret, baseObj) //sets stats
    turret
  }

  def cardIcon(owner: Player, cost: Int): GameObject = {
    UnitCreator.defaultCardIcon(texture, width, height, owner, cost)
  }

}
