package game.main.objects.brains

import com.badlogic.gdx.math.Vector2
import game.GameElement
import game.main.MainGame
import game.main.objects.improved.{PhysicsElement, UnitObject}
import game.util.Vector2e._
import game.util.pools

class AvoidObstacles(maxForceAvoid: Float, maxSeeAhead: Float) extends UnitElement {

  override def update(delta: Int): Unit = {

    //if no obstacle at close distance found, try look further away
    val avoid = pools.VectorPool.obtain()
    val obstacleFound = avoidObstacles(pUnit, 0, avoid)

    //if enough speed, try to look further away
    if (!obstacleFound && pUnit.movingForce.len2() > pUnit.maxMovingForce / 100f) {
      avoidObstacles(pUnit, maxSeeAhead, avoid)
    }

    //adds avoid force to moving force
    pUnit.movingForce.mulAdd(avoid, pUnit.ticker.delta)

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
      obj.physics.physWorld.collideCircle(obj.physics, ahead,
        obj.physics.collBody.getRadiusScaled,
        obj.physics.collFilter)

    //draws debug circle
    if (MainGame.drawCollBox)
      MainGame.debugRender.circle(ahead.x, ahead.y, obj.physics.collBody.getRadiusScaled)

    //calculate the force opposite to obstacle center
    obstacle.foreach(o => ((avoid ++ obj.pos) -- o.collBody.center).nor ** maxForceAvoid / obj.physics.mass)
    pools.VectorPool.free(ahead) //free the memory

    obstacle.isDefined

    false
  }

}
