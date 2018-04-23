package game.main.objects.brains

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.main.MainGame
import game.main.objects.UnitObject
import game.main.physics.collision.CollisionBody

class ShootAhead(attackVision: CollisionBody, reloadTime: Int) extends UnitElement {

  private var reloadTimer: Int = 0
  private var attackTarget: Option[UnitObject] = None

  override def update(delta: Int): Unit = {

    pUnit.physics.updateCollPolygon(attackVision) //update vision

    //finds a enemy
    if (attackTarget.isEmpty) {
      val enemy = pUnit.physics.physWorld.collideCollisionBody(pUnit.physics, attackVision, null, pUnit.owner.enemiesAsGameElement)
      enemy.foreach {
        case enemy: UnitObject => attackTarget = Some(enemy)
        case _ => Unit
      }
    }

    //checks shooting and if enemy is still possible target
    attackTarget.foreach(target => {
      if (target.canBeDeleted) attackTarget = None
      else {

        pUnit.moveTarget.set(target.pos)
        val blockingWall = pUnit.physics.physWorld.collideLine(pUnit.physics, pUnit.pos, target.pos, pUnit.physics.physWorld.mapFilter)

        if (blockingWall.isDefined) attackTarget = None
        else if (reloadTimer >= reloadTime) {
          pUnit.shoot()
          reloadTimer = 0 //reset the timers
        }
      }
    })

    if (MainGame.drawCollBox) attackVision.draw(MainGame.debugRender)

    reloadTimer = math.min(reloadTimer + delta, reloadTime)

  }
}
