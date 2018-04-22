package game.main.units

import java.util.PrimitiveIterator.OfDouble

import game.loader.GameTextures
import game.main.objects.UnitObject

object SoldierCreator extends UnitCreator {

  override val cost: Int = 3

  override val texture: Seq[String] = GameTextures.Units.unit1
  override val width: Float = 100f / 1.5f
  override val height: Float = 75f / 1.5f

  /** Sets the all stats to to unit */
  override protected def setStats(obj: UnitObject): Unit = {

    obj.mass = 100f
    obj.friction = 0.25f

    obj.maxForwardForce = 0.033f
    obj.maxAccelerateForce = 0.008f
    obj.maxSeeAhead = obj.sWidth * 2f

    obj.maxForceAvoid = 0.025f
    obj.maxRotateTime = 150f

    obj.health = 100f
    obj.damage = 30f
    obj.reloadTime = 150

    obj.visionMaxHeight = obj.sHeight * 3.5f
    obj.visionMaxDist = obj.sWidth * 4f

    obj.maxForwardForceAttack = obj.maxForwardForce * 0.5f

  }
}
