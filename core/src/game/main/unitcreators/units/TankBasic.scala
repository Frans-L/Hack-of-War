package game.main.unitcreators.units

import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies.{CollisionBody, PolygonBody}
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.GameObject
import game.main.gameworld.gameobject.objects.elements.unit.ai._
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.gameworld.gameobject.objects.elements.IconTextureElement
import game.main.players.Player
import game.main.unitcreators._
import game.main.unitcreators.bullet.BasicBullet
import game.util.Vector2e

object TankBasic extends SoldierCreator {

  override val cost: Int = 50

  override protected lazy val texture: UnitTextures = GameTextures.Units.Tank
  private val scale = 1.1f
  override protected lazy val width: Float = 120f / scale
  override protected lazy val height: Float = 75f / scale

  protected def collBody: CollisionBody = PolygonBody.rectangleCollBody(0, 0, width, height)

  override protected def setStats(obj: UnitObject, owner: Player, path: Path): Unit = {

    //units stats
    obj.mass = 750f
    obj.friction = 0.4f
    obj.health = 800f
    obj.maxMovingForce = 25f / 1000f

    //elements stats
    val acceptPathPointDist = obj.collBody.getRadiusScaled * 2.5f
    val steeringMass = 1000f
    val acceleration = 12f / 1000f
    val avoidForce = 10f / 1000f
    val avoidDistance = obj.sWidth * 1.0f
    val turnSpeed = 300f
    val stopNearBuildingDist = obj.collBody.getRadiusScaled * 5.5f

    //add elements
    obj.appendElement(new FollowPath(path, acceptPathPointDist))
    obj.appendElement(new Steering(steeringMass, acceleration))
    obj.appendElement(new AvoidObstacles(avoidForce, avoidDistance))
    obj.appendElement(new TurnToMovingDirection(turnSpeed))
    obj.appendElement(new FindBuildings(stopNearBuildingDist))

    //add turret
    val turret = TankTurretSmall.create(owner, obj)
    turret.pos.set(0, 0)
    obj.appendElement(turret)

  }

  override def cardIcon(owner: Player, cost: Int): GameObject = {
    val icon = TankTurretSmall.cardIcon(owner, cost)
    val building = UnitCreator.defaultCardIcon(texture, width, height, owner, cost)
    building.pos.set(0, 0)
    icon.prependElement(building)
    icon
  }

}

object TankTurretSmall extends TurretCreator {

  override protected lazy val texture: UnitTextures = GameTextures.Units.SoldierBasic
  private val scale = 1.5f
  override protected lazy val width: Float = 90f / scale
  override protected lazy val height: Float = 70f / scale

  override def setStats(turret: UnitObject, baseObj: UnitObject): Unit = {
    //element stats
    val damage = 30f
    val reloadTime = 600
    val turnSpeed = 350f

    val visionMinHeight = baseObj.sHeight * 2f
    val visionMaxHeight = baseObj.sHeight * 5f
    val visionMaxDist = baseObj.sWidth * 4f
    val attackVision =
      PolygonBody.trapezoidCollBody(visionMinHeight, visionMaxHeight, visionMaxDist,
        0, -(visionMinHeight - baseObj.sHeight) / 2)

    //add elements
    turret.appendElement(new ShootAhead(attackVision, damage, reloadTime, BasicBullet))
    turret.appendElement(new TurnToTarget(turnSpeed))
  }

}
