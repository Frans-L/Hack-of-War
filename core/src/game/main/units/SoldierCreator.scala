package game.main.units

import com.badlogic.gdx.Gdx
import game.loader.{GameTextures, UnitTextures}
import game.main.gamemap.Path
import game.main.gameobject.elements.ai._
import game.main.gameobject.objects.UnitObject
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

    obj.maxMovingForce = 0.030f

    //add brains
    obj.appendElement(new FollowPath(path, obj.collBody.getRadiusScaled * 1.5f))
    obj.appendElement(new ShootAhead(attackVision, 30f, 200, BasicBullet))
    obj.appendElement(new Steering(0.006f))
    obj.appendElement(new AvoidObstacles(0.020f, obj.sWidth * 1.25f))
    obj.appendElement(new SmoothTurn(150f))

  }
}
