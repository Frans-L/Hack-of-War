package game.main.gameworld.gameobject.objects.elements.unit.ai

import com.badlogic.gdx.Gdx
import game.main.gameworld.gameobject
import game.main.gameworld.gameobject.objects.elements.unit.UnitElement
import game.main.gameworld.gameobject.objects
import game.main.gameworld.gameobject.objects.UnitObject
import game.util.Vector2e._
import game.util.pools

/** Adds steering to the unit. Unit needs moveTarget. */
class Steering(steeringMass: Float, maxAccelerateForce: Float) extends UnitElement {

  /** Updates the movement with collision avoidance */
  override def update(p: gameobject.GameObject, delta: Int): Unit = {
    val parent = p.asInstanceOf[objects.UnitObject]


    val target = pools.VectorPool.obtain(parent.moveTarget) //pools.VectorPool.obtain(selectSteeringTarget())

    //moves towards target
    val steering =
      ((pools.VectorPool.obtain(target) -- parent.pos).nor ** parent.maxMovingForce) -- parent.movingForce

    //limit max acceleration speed
    steering.limit(maxAccelerateForce)

    //smooth it out with mass
    steering / steeringMass

    //adds steering to moving velocity
    parent.movingForce.mulAdd(steering, delta)

    //frees the memory
    pools.VectorPool.free(steering)
    pools.VectorPool.free(target)

  }

}
