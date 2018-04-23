package game.main.objects.brains

import game.main.objects.UnitObject
import game.util.Vector2e._
import game.util.pools

class Steering(maxAccelerateForce: Float) extends Brain {

  /** Updates the movement with collision avoidance */
  override def update(obj: UnitObject): Unit = {

    val target = pools.VectorPool.obtain(obj.moveTarget) //pools.VectorPool.obtain(selectSteeringTarget())

    //moves towards target
    val steering =
      (((pools.VectorPool.obtain(target) -- obj.pos).nor ** obj.maxMovingForce) -- obj.movingForce)
        .limit(maxAccelerateForce) / obj.mass

    //adds steering to moving velocity
    obj.movingForce.mulAdd(steering, obj.ticker.delta)

    //frees the memory
    pools.VectorPool.free(steering)
    pools.VectorPool.free(target)

  }


}
