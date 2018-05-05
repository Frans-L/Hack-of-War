package game.main.gameworld.gameobject.elements.unit.ai

import game.main.gameworld.gameobject
import game.main.gameworld.gameobject.elements.unit.UnitElement
import game.main.gameworld.gameobject.objects
import game.util.Vector2e._
import game.util.pools

class Steering(maxAccelerateForce: Float) extends UnitElement {

  /** Updates the movement with collision avoidance */
  override def update(p: gameobject.GameObject, delta: Int): Unit = {
    val parent = p.asInstanceOf[objects.UnitObject]


    val target = pools.VectorPool.obtain(parent.moveTarget) //pools.VectorPool.obtain(selectSteeringTarget())

    //moves towards target
    val steering =
      (((pools.VectorPool.obtain(target) -- parent.pos).nor ** parent.maxMovingForce) -- parent.movingForce)
        .limit(maxAccelerateForce) / parent.mass

    //adds steering to moving velocity
    parent.movingForce.mulAdd(steering, delta)

    //frees the memory
    pools.VectorPool.free(steering)
    pools.VectorPool.free(target)

  }

}
