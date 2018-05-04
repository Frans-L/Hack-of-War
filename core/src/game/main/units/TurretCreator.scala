package game.main.units

import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies.CircleBody
import game.main.gameworld.gameobject.elements.{TextureElement, unit}
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.players.Player

trait TurretCreator {

  protected val texture: UnitTextures
  protected val width: Float
  protected val height: Float

  def setStats(turret: UnitObject, baseObj: UnitObject): Unit

  def create(owner: Player, baseObj: UnitObject): UnitObject = {

    val collBody = new CircleBody(2)
    val turret = UnitCreator.createUnit(owner, collBody, width, height, update = false)

    turret.opacity = 0.75f
    turret.angle = baseObj.angle
    turret.origin.set(turret.size.x / 3f, turret.size.y / 2f)

    //add textures
    turret.appendElement(
      new TextureElement(GameTextures.default.atlas.findRegion(texture.main(baseObj.owner.colorIndex))))

    setStats(turret, baseObj) //sets stats
    turret
  }

}

object BuildingTurretSmall extends TurretCreator {

  override protected lazy val texture: UnitTextures = GameTextures.Units.BaseSoldier
  override protected lazy val width: Float = 90f / 1.5f
  override protected lazy val height: Float = 70f / 1.5f

  override def setStats(turret: UnitObject, baseObj: UnitObject): Unit = {
    //element stats
    val damage = 10f
    val reloadTime = 300
    val turnSpeed = 350f
    val visionMaxDist = baseObj.sWidth * 4f
    val attackVision = new CircleBody(visionMaxDist)

    //add elements
    turret.appendElement(new unit.ShootAhead(attackVision, damage, reloadTime, BasicBullet))
    turret.appendElement(new unit.TurnToTarget(turnSpeed))
  }

}

object BuildingTurretMain extends TurretCreator {

  override protected lazy val texture: UnitTextures = GameTextures.Units.BaseSoldier
  override protected lazy val width: Float = 90f / 1.5f
  override protected lazy val height: Float = 70f / 1.5f

  override def setStats(turret: UnitObject, baseObj: UnitObject): Unit = {
    //element stats
    val damage = 15f
    val reloadTime = 300
    val turnSpeed = 350f
    val visionMaxDist = baseObj.sWidth * 4f
    val attackVision = new CircleBody(visionMaxDist)

    //add elements
    turret.appendElement(new unit.ShootAhead(attackVision, damage, reloadTime, BasicBullet))
    turret.appendElement(new unit.TurnToTarget(turnSpeed))
  }

}

