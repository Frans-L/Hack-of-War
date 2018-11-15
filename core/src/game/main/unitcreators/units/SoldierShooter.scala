package game.main.unitcreators.units

import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies.{CollisionBody, PolygonBody}
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.gameworld.gameobject.objects.UnitObject.AIScore
import game.main.gameworld.gameobject.objects.elements.unit.ai._
import game.main.players.Player
import game.main.unitcreators.SoldierCreator
import game.main.unitcreators.bullet.BasicBullet

object SoldierShooter extends SoldierCreator {

  override val cost: Int = 50

  override protected lazy val texture: UnitTextures = GameTextures.Units.SoldierShooter
  private val scale = 1.5f
  override protected lazy val width: Float = 100f / scale
  override protected lazy val height: Float = 75f / scale
  private lazy val heightMin: Float = 20f / scale

  protected def collBody: CollisionBody =
    PolygonBody.trapezoidCollBody(height, heightMin, width)

  override protected def setStats(obj: UnitObject, owner: Player, path: Path): Unit = {

    //units stats
    obj.mass = 150f
    obj.friction = 0.25f
    obj.health = 65f
    obj.maxMovingForce = 60f / 1000f

    //elements stats
    val damage = 20f
    val reloadTime = 175
    val steeringMass = 100f
    val acceleration = 9f / 1000f

    val acceptPathPointDist = obj.collBody.getRadiusScaled * 1.5f
    val avoidForce = 20f / 1000f
    val avoidDistance = obj.sWidth * 1.5f
    val turnTime = 150f

    val visionMaxHeight = obj.sHeight * 4.5f
    val visionMaxDist = obj.sWidth * 6f
    val attackVision = PolygonBody.trapezoidCollBody(obj.sHeight, visionMaxHeight, visionMaxDist)
    val attackMovingMultiplier = 0.90f
    val attackStopMovingDist = visionMaxDist / 1.2f

    //add elements
    obj.appendElement(new FollowPath(path, acceptPathPointDist))
    obj.appendElement(new ShootAhead(attackVision, damage, reloadTime, BasicBullet))
    obj.appendElement(new Steering(steeringMass, acceleration))
    obj.appendElement(new AvoidObstacles(avoidForce, avoidDistance))
    obj.appendElement(new TurnToMovingDirection(turnTime))
    obj.appendElement(new MoveWhileAttacking(attackMovingMultiplier, attackStopMovingDist))

  }

  override val aiScore: AIScore = new AIScore {
    override val attackLight: Float = 50
    override val attackHeavy: Float = 30
    override val light: Float = 0.75f
    override val heavy: Float = 0.25f
    override val priority: Float = 110f
    override val string = "SoldierShooter"
  }

}
