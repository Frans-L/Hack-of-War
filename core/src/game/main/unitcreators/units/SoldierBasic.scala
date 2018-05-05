package game.main.unitcreators.units

import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies.{CollisionBody, PolygonBody}
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.objects.elements.unit.ai._
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.players.Player
import game.main.unitcreators.SoldierCreator
import game.main.unitcreators.bullet.BasicBullet

object SoldierBasic extends SoldierCreator {

  override val cost: Int = 30

  override protected lazy val texture: UnitTextures = GameTextures.Units.SoldierBasic
  private val scale = 1.5f
  override protected lazy val width: Float = 100f / scale
  override protected lazy val height: Float = 75f / scale

  protected def collBody: CollisionBody = PolygonBody.triangleCollBody(width, height / 2f, 0, height)

  override protected def setStats(obj: UnitObject, owner: Player, path: Path): Unit = {

    //units stats
    obj.mass = 100f
    obj.friction = 0.25f
    obj.health = 125f
    obj.maxMovingForce = 30f / 1000f

    //elements stats
    val damage = 30f
    val reloadTime = 300
    val steeringMass = 100f
    val acceleration = 6f / 1000f

    val acceptPathPointDist = obj.collBody.getRadiusScaled * 1.5f
    val avoidForce = 20f / 1000f
    val avoidDistance = obj.sWidth * 1.5f
    val turnTime = 150f

    val visionMaxHeight = obj.sHeight * 3.5f
    val visionMaxDist = obj.sWidth * 4f
    val attackVision = PolygonBody.trapezoidCollBody(obj.sHeight, visionMaxHeight, visionMaxDist)
    val attackMovingMultiplier = 0.925f
    val attackStopMovingDist = visionMaxDist / 1.5f

    //add elements
    obj.appendElement(new FollowPath(path, acceptPathPointDist))
    obj.appendElement(new ShootAhead(attackVision, damage, reloadTime, BasicBullet))
    obj.appendElement(new Steering(steeringMass, acceleration))
    obj.appendElement(new AvoidObstacles(avoidForce, avoidDistance))
    obj.appendElement(new TurnToMovingDirection(turnTime))
    obj.appendElement(new MoveWhileAttacking(attackMovingMultiplier, attackStopMovingDist))

  }
}
