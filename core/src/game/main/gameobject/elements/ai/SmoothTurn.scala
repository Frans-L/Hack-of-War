package game.main.gameobject.elements.ai

import com.badlogic.gdx.math.Interpolation
import game.main.gameobject.objects.UnitObject
import game.main.gameobject.{GameObject, ObjectElement}
import game.util.Utils

class SmoothTurn(maxRotateTime: Float) extends ObjectElement {

  override def update(p: GameObject, delta: Int): Unit = {
    val parent = p.asInstanceOf[UnitObject]

    //rotates smoothly towards moving direction
    parent.angle = Utils.closestAngle360(parent.angle, parent.movingForce.angle)
    parent.angle = Interpolation.linear.apply(parent.angle, parent.movingForce.angle, delta / maxRotateTime)
    parent.angle = Utils.absAngle(parent.angle)
  }

  /** Throws an error if the parent is not valid! */
  override def checkParent(parent: GameObject): Unit =
    require(parent.isInstanceOf[UnitObject], "Parent have to be UnitObject")

}
