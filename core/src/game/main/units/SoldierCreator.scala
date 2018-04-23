package game.main.units

import java.util.PrimitiveIterator.OfDouble

import game.loader.{GameTextures, UnitTextures}
import game.main.gameMap.{IconPath, Path}
import game.main.objects.UnitObject
import game.main.objects.brains._
import game.main.physics.collision.PolygonBody

object SoldierCreator extends UnitCreator {

  override val cost: Int = 3

  override val texture: UnitTextures = GameTextures.Units.BaseSoldier
  override val width: Float = 100f / 1.5f
  override val height: Float = 75f / 1.5f

  /** Sets the all stats to to unit */
  override protected def setStats(obj: UnitObject, path: Path): Unit = {

    obj.mass = 100f
    obj.friction = 0.25f

    obj.health = 100f
    obj.damage = 30f
    obj.bulletCreator = BasicBullet

    val visionMaxHeight = obj.sHeight * 3.5f
    val visionMaxDist = obj.sWidth * 4f
    val attackVision = PolygonBody.trapezoidCollBody(obj.sHeight, visionMaxHeight, visionMaxDist)

    obj.maxMovingForce = 0.033f
    obj.maxForwardForceAttack = obj.maxMovingForce * 0.5f

    //add brains
    obj.brains += new FollowPath(path, obj.collBody.getRadiusScaled * 2)
    obj.brains += new ShootAhead(attackVision, 150)
    obj.brains += new Steering(0.008f)
    obj.brains += new AvoidObstacles(obj.maxMovingForce*0.75f, obj.sWidth * 2f)
    obj.brains += new SmoothTurn(150f)

  }
}
