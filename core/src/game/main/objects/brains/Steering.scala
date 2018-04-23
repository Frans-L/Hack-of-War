package game.main.objects.brains

import game.main.objects.UnitObject
import game.util.Vector2e._
import game.util.pools

class Steering(maxAccelerateForce: Float) extends UnitElement {

  /** Updates the movement with collision avoidance */
  override def update(delta: Int): Unit = {

    val target = pools.VectorPool.obtain(pUnit.moveTarget) //pools.VectorPool.obtain(selectSteeringTarget())

    //moves towards target
    val steering =
      (((pools.VectorPool.obtain(target) -- pUnit.pos).nor ** pUnit.maxMovingForce) -- pUnit.movingForce)
        .limit(maxAccelerateForce) / pUnit.physics.mass

    //adds steering to moving velocity
    pUnit.movingForce.mulAdd(steering, delta)

    //frees the memory
    pools.VectorPool.free(steering)
    pools.VectorPool.free(target)

  }


}
