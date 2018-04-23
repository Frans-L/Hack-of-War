package game.main.objects.brains

import com.badlogic.gdx.math.Interpolation
import game.main.objects.UnitObject
import game.util.Utils

class SmoothTurn(maxRotateTime: Float) extends Brain {

  override def update(obj: UnitObject): Unit = {
    //rotates smoothly towards moving direction
    obj.angle = Utils.closestAngle360(obj.angle, obj.movingForce.angle)
    obj.angle = Interpolation.linear.apply(obj.angle, obj.movingForce.angle, obj.ticker.delta / maxRotateTime)
    obj.angle = Utils.absAngle(obj.angle)
  }

}
