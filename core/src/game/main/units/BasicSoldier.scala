package game.main.units

import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies.{CollisionBody, PolygonBody}
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.elements.unit
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.players.Player

object BasicSoldier extends SoldierCreator {

  override val cost: Int = 4

  override protected lazy val texture: UnitTextures = GameTextures.Units.BaseSoldier
  override protected lazy val width: Float = 100f / 1.5f
  override protected lazy val height: Float = 75f / 1.5f

  protected def collBody: CollisionBody = PolygonBody.triangleCollBody(width, height / 2f, 0, height)

  override protected def setStats(obj: UnitObject, owner: Player, path: Path): Unit = {

    //units stats
    obj.mass = 100f
    obj.friction = 0.25f
    obj.health = 100f
    obj.maxMovingForce = 0.030f

    //elements stats
    val damage = 30f
    val reloadTime = 200
    val acceleration = 0.006f
    val avoidForce = 0.020f
    val avoidDistance = obj.sWidth * 1.5f
    val turnSpeed = 150f
    val visionMaxHeight = obj.sHeight * 3.5f
    val visionMaxDist = obj.sWidth * 4f
    val attackVision = PolygonBody.trapezoidCollBody(obj.sHeight, visionMaxHeight, visionMaxDist)

    //add elements
    obj.appendElement(new unit.FollowPath(path, obj.collBody.getRadiusScaled * 1.5f))
    obj.appendElement(new unit.ShootAhead(attackVision, damage, reloadTime, BasicBullet))
    obj.appendElement(new unit.Steering(acceleration))
    obj.appendElement(new unit.AvoidObstacles(avoidForce, avoidDistance))
    obj.appendElement(new unit.TurnToMovingDirection(turnSpeed))

  }
}
