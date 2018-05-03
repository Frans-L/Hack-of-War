package game.main.units

import com.badlogic.gdx.Gdx
import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies.{CollisionBody, PolygonBody}
import game.main.gameworld.gameobject.elements.ai
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.objects.UnitObject

object SoldierCreator extends UnitCreator {

  override lazy val cost: Int = 3

  override lazy val texture: UnitTextures = GameTextures.Units.BaseSoldier
  override lazy val width: Float = 100f / 1.5f
  override lazy val height: Float = 75f / 1.5f

  override def collBody: CollisionBody = PolygonBody.triangleCollBody(width, height / 2f, 0, height)

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
    obj.appendElement(new ai.FollowPath(path, obj.collBody.getRadiusScaled * 1.5f))
    obj.appendElement(new ai.ShootAhead(attackVision, 30f, 200, BasicBullet))
    obj.appendElement(new ai.Steering(0.006f))
    //obj.appendElement(new ai.AvoidObstacles(0.020f, obj.sWidth * 1.25f))
    obj.appendElement(new ai.SmoothTurn(150f))

  }
}
