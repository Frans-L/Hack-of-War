package game.main.units

import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies.{CircleBody, CollisionBody, PolygonBody}
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.GameObject
import game.main.gameworld.gameobject.elements.IconTextureElement
import game.main.gameworld.gameobject.elements.unit.ai._
import game.main.gameworld.gameobject.objects.{IconObject, UnitObject}
import game.main.players.Player
import game.main.units.BasicSoldier.{height, width}
import game.main.units.bullet.BasicBullet


object SwarmSoldier3 extends SwarmSoldier{

  /** Cost of the unit */
  override val cost: Int = 40
  override val unitAmount: Int = 3
}

object SwarmSoldier5 extends SwarmSoldier{

  override val cost: Int = 50
  override val unitAmount: Int = 5
}

trait SwarmSoldier extends SoldierCreator {

  val unitAmount: Int //amount of units

  override protected val texture: UnitTextures = GameTextures.Units.SoldierBasic
  private val scale: Float = 2.25f
  override protected val width: Float = 100f / scale
  override protected val height: Float = 75f / scale

  override protected def collBody: CollisionBody = PolygonBody.triangleCollBody(width, height / 2f, 0, height)

  override def create(owner: Player, x: Float, y: Float, path: Path, extraOffset: Float): Seq[UnitObject] = {

    val units = new Array[UnitObject](unitAmount)
    for (i <- units.indices) {
      val offset = extraOffset + i * (1 - (i % 2) * 2) * height //different offset to everyone
      units(i) = super.create(owner, x, y, path, offset).head
    }

    units
  }

  override protected def setStats(obj: UnitObject, owner: Player, path: Path): Unit = {

    //units stats
    obj.mass = 50f
    obj.friction = 0.25f
    obj.health = 50f
    obj.maxMovingForce = 35f / 1000f

    //elements stats
    val damage = 30f
    val reloadTime = 200
    val acceleration = 9f / 1000f
    val avoidForce = 40f / 1000f
    val avoidDistance = obj.sWidth * 1.2f
    val turnSpeed = 185f
    val visionMaxDist = obj.sWidth * 3f
    val attackVision = new CircleBody(visionMaxDist)

    //add elements
    obj.appendElement(new FollowPath(path, obj.collBody.getRadiusScaled * 1.5f))
    obj.appendElement(new ShootAhead(attackVision, damage, reloadTime, BasicBullet))
    obj.appendElement(new Steering(acceleration))
    obj.appendElement(new AvoidObstacles(avoidForce, avoidDistance))
    obj.appendElement(new TurnToMovingDirection(turnSpeed))
  }

  override def cardIcon(owner: Player, cost: Int): GameObject = {
    UnitCreator.defaultCardIcon(texture, width, height, owner, cost, unitAmount)
  }

}
