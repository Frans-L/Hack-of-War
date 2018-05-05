package game.main.gameworld.gameobject.objects.elements.unit.ai

import com.badlogic.gdx.Gdx
import game.main.gameworld.gameobject.GameObject
import game.main.gameworld.gameobject.objects.elements.unit.UnitElement
import game.main.gameworld.gameobject.objects.UnitObject
import game.util.Vector2e._


/** Limits speed when the object is attacking */
class MoveWhileAttacking(attackMovingMultiplier: Float, stopDistance: Float) extends UnitElement {

  override def update(p: GameObject, delta: Int): Unit = {
    val parent = p.asInstanceOf[UnitObject]

    if (parent.state == UnitObject.State.attack) {
      parent.movingForce ** attackMovingMultiplier

      if (parent.pos.dst2(parent.moveTarget) < stopDistance * stopDistance)
        parent.movingForce ** 0

    }
  }

}
