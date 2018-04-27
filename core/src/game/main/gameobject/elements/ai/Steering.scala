package game.main.gameobject.elements.ai

import game.main.gameobject.objects.UnitObject
import game.main.gameobject.{GameObject, ObjectElement}
import game.util.Vector2e._
import game.util.pools

class Steering(maxAccelerateForce: Float) extends ObjectElement {

  /** Updates the movement with collision avoidance */
  override def update(p: GameObject, delta: Int): Unit = {
    val parent = p.asInstanceOf[UnitObject]


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

  /** Throws an error if the parent is not valid! */
  override def checkParent(parent: GameObject): Unit =
    require(parent.isInstanceOf[UnitObject], "Parent have to be UnitObject")


}
