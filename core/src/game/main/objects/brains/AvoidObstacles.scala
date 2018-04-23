package game.main.objects.brains

import com.badlogic.gdx.math.Vector2
import game.main.MainGame
import game.main.objects.UnitObject
import game.util.Vector2e._
import game.util.pools

class AvoidObstacles(maxForceAvoid: Float, maxSeeAhead: Float) extends Brain {

  override def update(obj: UnitObject): Unit = {

    //if no obstacle at close distance found, try look further away
    val avoid = pools.VectorPool.obtain()
    val obstacleFound = avoidObstacles(obj, 0, avoid)

    //if enough speed, try to look further away
    if (!obstacleFound && obj.movingForce.len2() > obj.maxMovingForce / 100f) {
      avoidObstacles(obj, maxSeeAhead, avoid)
    }

    //adds avoid force to moving force
    obj.movingForce.mulAdd(avoid, obj.ticker.delta)

    pools.VectorPool.free(avoid)
  }

  /** Check if obstacles are ahead. Sets the 'avoid' Vector2 to
    * push the player around the obstacle
    *
    * @param visionLength distance to look ahead
    * @param avoid        a Vector2 that is set to push the player away from the obstacle
    * @return true if there were an obstacle
    */
  private def avoidObstacles(obj: UnitObject, visionLength: Float, avoid: Vector2): Boolean = {

    val ahead = pools.VectorPool.obtain(obj.movingForce).nor **
      (visionLength * (obj.movingForce.len() / obj.maxMovingForce)) ++ obj.pos

    //checks collision in the wanted pos
    val obstacle =
      obj.physWorld.collideCircle(obj, ahead, obj.collBody.getRadiusScaled, obj.collFilter)

    //draws debug circle
    if (MainGame.drawCollBox) MainGame.debugRender.circle(ahead.x, ahead.y, obj.collBody.getRadiusScaled)

    //calculate the force opposite to obstacle center
    obstacle.foreach(o => ((avoid ++ obj.pos) -- o.collBody.center).nor ** maxForceAvoid / obj.mass)
    pools.VectorPool.free(ahead) //free the memory

    obstacle.isDefined
  }

}
