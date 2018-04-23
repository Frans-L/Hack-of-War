package game.main.objects.brains

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.main.MainGame
import game.main.objects.UnitObject
import game.main.physics.collision.CollisionBody

class ShootAhead(attackVision: CollisionBody, reloadTime: Int) extends Brain {

  private var reloadTimer: Int = 0
  private var attackTarget: Option[UnitObject] = None

  override def update(obj: UnitObject): Unit = {

    obj.updateCollPolygon(attackVision) //update vision

    //finds a enemy
    if (attackTarget.isEmpty) {
      val enemy = obj.physWorld.collideCollisionBody(obj, attackVision, null, obj.owner.enemiesAsGameElement)
      enemy.foreach {
        case enemy: UnitObject => attackTarget = Some(enemy)
        case _ => Unit
      }
    }

    //checks shooting and if enemy is still possible target
    attackTarget.foreach(target => {
      if (target.canBeDeleted) attackTarget = None
      else {

        obj.moveTarget.set(target.pos)
        val blockingWall = obj.physWorld.collideLine(obj, obj.pos, target.pos, obj.physWorld.mapFilter)

        if (blockingWall.isDefined) attackTarget = None
        else if (reloadTimer >= reloadTime) {
          obj.shoot()
          reloadTimer = 0 //reset the timers
        }
      }
    })

    if (MainGame.drawCollBox) attackVision.draw(MainGame.debugRender)

    reloadTimer = math.min(reloadTimer + obj.ticker.delta, reloadTime)

  }
}
