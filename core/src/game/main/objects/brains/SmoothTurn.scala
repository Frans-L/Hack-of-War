package game.main.objects.brains

import com.badlogic.gdx.math.Interpolation
import game.main.objects.UnitObject
import game.util.Utils

class SmoothTurn(maxRotateTime: Float) extends UnitElement {

  override def update(delta: Int): Unit = {
    //rotates smoothly towards moving direction
    pUnit.angle = Utils.closestAngle360(pUnit.angle, pUnit.movingForce.angle)
    pUnit.angle = Interpolation.linear.apply(pUnit.angle, pUnit.movingForce.angle, delta / maxRotateTime)
    pUnit.angle = Utils.absAngle(pUnit.angle)
  }

}
