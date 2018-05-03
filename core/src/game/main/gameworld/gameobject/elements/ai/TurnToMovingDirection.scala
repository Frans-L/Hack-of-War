package game.main.gameworld.gameobject.elements.ai

import com.badlogic.gdx.math.Interpolation
import game.main.gameworld.gameobject
import game.main.gameworld.gameobject.{ObjectElement, objects}
import game.util.Utils

class TurnToMovingDirection(maxRotateTime: Float) extends UnitElement {

  override def update(p: gameobject.GameObject, delta: Int): Unit = {
    val parent = p.asInstanceOf[objects.UnitObject]

    //rotates smoothly towards moving direction
    parent.angle = Utils.closestAngle360(parent.angle, parent.movingForce.angle)
    parent.angle = Interpolation.linear.apply(parent.angle, parent.movingForce.angle, delta / maxRotateTime)
    parent.angle = Utils.absAngle(parent.angle)
  }

}
