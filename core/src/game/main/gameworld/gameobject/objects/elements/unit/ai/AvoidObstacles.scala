package game.main.gameworld.gameobject.objects.elements.unit.ai

import com.badlogic.gdx.math.Vector2
import game.main.MainGame
import game.main.gameworld.gameobject
import game.main.gameworld.gameobject.objects.elements.unit.UnitElement
import game.main.gameworld.gameobject.objects
import game.util.Vector2e._
import game.util.pools

class AvoidObstacles(maxForceAvoid: Float, maxSeeAhead: Float) extends UnitElement {

  override def update(p: gameobject.GameObject, delta: Int): Unit = {
    val parent = p.asInstanceOf[objects.UnitObject]

    //if no obstacle at close distance found, try look further away
    val avoid = pools.VectorPool.obtain()
    val obstacleFound = avoidObstacles(parent, 0, avoid)

    //if enough speed, try to look further away
    if (!obstacleFound && parent.movingForce.len2() > parent.maxMovingForce * parent.maxMovingForce / 10f) {
      avoidObstacles(parent, maxSeeAhead, avoid)
    }

    //adds avoid force to moving force
    parent.movingForce.mulAdd(avoid, delta)

    pools.VectorPool.free(avoid)
  }

  /** Check if obstacles are ahead. Sets the 'avoid' Vector2 to
    * push the player around the obstacle
    *
    * @param visionLength distance to look ahead
    * @param avoid        a Vector2 that is set to push the player away from the obstacle
    * @return true if there were an obstacle
    */
  private def avoidObstacles(obj: objects.UnitObject, visionLength: Float, avoid: Vector2): Boolean = {


    val ahead = pools.VectorPool.obtain(obj.movingForce).nor **
      (visionLength * (obj.movingForce.len() / obj.maxMovingForce)) ++ obj.pos

    //checks collision in the wanted pos
    val obstacle =
      obj.collHandler.collideCircle(obj, ahead,
        obj.collBody.getRadiusScaled,
        obj.collFilter)

    //draws debug circle
    if (MainGame.drawCollBox){
      MainGame.debugRender.circle(ahead.x, ahead.y, obj.collBody.getRadiusScaled)
      //obstacle.foreach(o => MainGame.debugRender.circle(o.collBody.center.x, o.collBody.center.y, 10))
    }

    //calculate the force opposite to obstacle center
    obstacle.foreach(o => ((avoid ++ obj.pos) -- o.collBody.center).nor ** maxForceAvoid / obj.mass)
    pools.VectorPool.free(ahead) //free the memory

    obstacle.isDefined

    false
  }

}
