package game.main.gameworld.gameobject.elements.unit.ai

import com.badlogic.gdx.math.Interpolation
import game.main.gameworld.gameobject
import game.main.gameworld.gameobject.elements.unit.UnitElement
import game.main.gameworld.gameobject.objects
import game.util.{Utils, Vector2e}

class TurnToTarget(maxRotateTime: Float) extends UnitElement {

  private val tmpVec = Vector2e(0, 0)

  override def update(p: gameobject.GameObject, delta: Int): Unit = {
    val parent = p.asInstanceOf[objects.UnitObject]

    //rotates smoothly towards moving direction
    tmpVec.set(parent.moveTarget).sub(parent.pos)
    parent.angle = Utils.closestAngle360(parent.angle, tmpVec.angle)
    parent.angle = Interpolation.linear.apply(parent.angle, tmpVec.angle, delta / maxRotateTime)
    parent.angle = Utils.absAngle(parent.angle)

  }

}
