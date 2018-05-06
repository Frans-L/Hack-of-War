package game.main.unitcreators.units

import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies.{CircleBody, CollisionBody, PolygonBody}
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.objects.elements.unit.HealthBarElement
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.gameworld.gameobject.objects.UnitObject.AIScore
import game.main.gameworld.gameobject.objects.elements.unit.ai.{ShootAhead, TurnToTarget}
import game.main.players.Player
import game.main.unitcreators.bullet.BasicBullet
import game.main.unitcreators.{BuildingCreator, TurretCreator}

object BuildingMain extends BuildingCreator {

  override val cost: Int = 20
  override val aiScore: AIScore = UnitObject.AIScoreNone

  override protected lazy val texture: UnitTextures = GameTextures.Units.BuildingLong
  override protected lazy val width: Float = 80f
  override protected lazy val height: Float = 200f

  override protected def collBody: CollisionBody = PolygonBody.rectangleCollBody(0, 0, width, height)

  override protected def setStats(obj: UnitObject, owner: Player, path: Path): Unit = {

    obj.mass = 300
    obj.health = 1500
    obj.static = true

    obj.appendElement(BuildingTurretMain.create(owner, obj))
    obj.lastElement[UnitObject].pos.set(0, height / 3f)
    obj.appendElement(BuildingTurretMain.create(owner, obj))
    obj.lastElement[UnitObject].pos.set(0, -height / 3f)

    obj.appendElement(new HealthBarElement(obj.health))
  }

}

object BuildingTurretMain extends TurretCreator {

  override protected lazy val texture: UnitTextures = GameTextures.Units.SoldierBasic
  private val scale = 1.5f
  override protected lazy val width: Float = 90f / scale
  override protected lazy val height: Float = 70f / scale

  override def setStats(turret: UnitObject, baseObj: UnitObject): Unit = {
    //element stats
    val damage = 20f
    val reloadTime = 300
    val turnSpeed = 350f
    val visionMaxDist = baseObj.sWidth * 4.25f
    val attackVision = new CircleBody(visionMaxDist)

    //add elements
    turret.appendElement(new ShootAhead(attackVision, damage, reloadTime, BasicBullet))
    turret.appendElement(new TurnToTarget(turnSpeed))
  }

}
