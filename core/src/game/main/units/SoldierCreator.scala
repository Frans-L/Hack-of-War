package game.main.units

import java.util.PrimitiveIterator.OfDouble

import com.badlogic.gdx.Gdx
import game.loader.{GameTextures, UnitTextures}
import game.main.gameMap.{IconPath, Path}
import game.main.objects.brains._
import game.main.objects.improved.UnitObject
import game.main.physics.collision.PolygonBody

object SoldierCreator extends UnitCreator {

  override val cost: Int = 3

  override val texture: UnitTextures = GameTextures.Units.BaseSoldier
  override val width: Float = 100f / 1.5f
  override val height: Float = 75f / 1.5f

  /** Sets the all stats to to unit */
  override protected def setStats(obj: UnitObject, path: Path): Unit = {

    /*
    obj.maxForwardForceAttack = obj.maxMovingForce * 0.5f
    */

    obj.mass = 100f
    obj.friction = 0.25f

    obj.health = 100f

    val visionMaxHeight = obj.sHeight * 3.5f
    val visionMaxDist = obj.sWidth * 4f
    val attackVision = PolygonBody.trapezoidCollBody(obj.sHeight, visionMaxHeight, visionMaxDist)

    obj.maxMovingForce = 0.033f

    //add brains
    obj.appendElement(new FollowPath(path, obj.collBody.getRadiusScaled * 1.5f))
    obj.appendElement(new ShootAhead(attackVision, 30f, 200, BasicBullet))
    obj.appendElement(new Steering(0.008f))
    obj.appendElement(new AvoidObstacles(0.020f, obj.sWidth * 1.5f))
    obj.appendElement(new SmoothTurn(150f))

  }
}
