package game.main.unitcreators.units

import com.badlogic.gdx.Gdx
import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies.{CollisionBody, PolygonBody}
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.GameObject
import game.main.gameworld.gameobject.objects.elements.unit.ai._
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.gameworld.gameobject.objects.UnitObject.AIScore
import game.main.players.Player
import game.main.unitcreators._
import game.main.unitcreators.bullet.BasicBullet
import game.util.Vector2e

object TankBasic extends SoldierCreator {

  override val cost: Int = 60

  override protected lazy val texture: UnitTextures = GameTextures.Units.Tank
  private val scale = 1.1f
  override protected lazy val width: Float = 120f / scale
  override protected lazy val height: Float = 75f / scale

  protected def collBody: CollisionBody = PolygonBody.rectangleCollBody(0, 0, width, height)

  override protected def setStats(obj: UnitObject, owner: Player, path: Path): Unit = {

    //units stats
    obj.category = UnitObject.Category.tank
    obj.mass = 300f
    obj.friction = 0.25f
    obj.health = 600f
    obj.maxMovingForce = 20f / 1000f

    //elements stats
    val acceptPathPointDist = obj.collBody.getRadiusScaled * 2.25f
    val steeringMass = 1000f
    val acceleration = 6f / 1000f
    val avoidForce = 18f / 1000f
    val avoidDistance = obj.sWidth * 1.0f
    val turnSpeed = 300f
    val stopNearBuildingDist = obj.collBody.getRadiusScaled * 4.75f

    //add elements
    obj.appendElement(new FollowPath(path, acceptPathPointDist))
    obj.appendElement(new Steering(steeringMass, acceleration))
    obj.appendElement(new AvoidObstacles(avoidForce, avoidDistance))
    obj.appendElement(new TurnToMovingDirection(turnSpeed))
    obj.appendElement(new FindHeavyTargets(stopNearBuildingDist))

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

  override val aiScore: AIScore = new AIScore {
    override val attackLight: Float = 45
    override val attackHeavy: Float = 45
    override val light: Float = 0f
    override val heavy: Float = 1f
    override val priority: Float = 105f
    override val string = "TankBasic"
  }

}

object TankTurretSmall extends TurretCreator {

  override protected lazy val texture: UnitTextures = GameTextures.Units.SoldierBasic
  private val scale = 1.5f
  override protected lazy val width: Float = 90f / scale
  override protected lazy val height: Float = 70f / scale

  override def setStats(turret: UnitObject, baseObj: UnitObject): Unit = {
    //element stats
    val damage = 55f
    val reloadTime = 900
    val turnSpeed = 350f

    val visionMinHeight = baseObj.sHeight * 2f
    val visionMaxHeight = baseObj.sHeight * 5f
    val visionMaxDist = baseObj.sWidth * 3.5f
    val attackVision =
      PolygonBody.trapezoidCollBody(visionMinHeight, visionMaxHeight, visionMaxDist,
        0, -(visionMinHeight - baseObj.sHeight) / 2)

    //add elements
    turret.appendElement(new ShootAhead(attackVision, damage, reloadTime, BasicBullet))
    turret.appendElement(new TurnToTarget(turnSpeed))
  }

}
