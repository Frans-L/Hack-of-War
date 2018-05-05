package game.main.unitcreators.units

import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies.{CircleBody, CollisionBody}
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.objects.elements.unit.HealthBarElement
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.gameworld.gameobject.objects.elements.unit.ai.{ShootAhead, TurnToTarget}
import game.main.players.Player
import game.main.unitcreators.bullet.BasicBullet
import game.main.unitcreators.{BuildingCreator, TurretCreator}


object BuildingLane extends BuildingCreator {

  override val cost: Int = 10

  override protected lazy val texture: UnitTextures = GameTextures.Units.BuildingSmall
  override protected lazy val width: Float = 75f
  override protected lazy val height: Float = 75f

  override protected def collBody: CollisionBody = new CircleBody(width / 2f)

  override protected def setStats(obj: UnitObject, owner: Player, path: Path): Unit = {

    obj.mass = 300
    obj.health = 900
    obj.static = true

    obj.appendElement(BuildingTurretSmall.create(owner, obj))
    obj.appendElement(new HealthBarElement(obj.health))
  }

}


object BuildingTurretSmall extends TurretCreator {

  override protected lazy val texture: UnitTextures = GameTextures.Units.SoldierBasic
  private val scale = 1.5f
  override protected lazy val width: Float = 90f / scale
  override protected lazy val height: Float = 70f / scale

  override def setStats(turret: UnitObject, baseObj: UnitObject): Unit = {
    //element stats
    val damage = 15f
    val reloadTime = 300
    val turnSpeed = 350f
    val visionMaxDist = baseObj.sWidth * 4f
    val attackVision = new CircleBody(visionMaxDist)

    //add elements
    turret.appendElement(new ShootAhead(attackVision, damage, reloadTime, BasicBullet))
    turret.appendElement(new TurnToTarget(turnSpeed))
  }

}


