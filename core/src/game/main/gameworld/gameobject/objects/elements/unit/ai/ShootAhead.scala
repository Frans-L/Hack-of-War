package game.main.gameworld.gameobject.objects.elements.unit.ai

import game.GameElement
import game.main.MainGame
import game.main.gameworld.collision.bodies.CollisionBody
import game.main.gameworld.gameobject.GameObject
import game.main.gameworld.gameobject.objects.elements.unit.UnitElement
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.unitcreators.bullet.BulletCreator
import game.util.Vector2e
import game.util.Vector2e._

import scala.collection.mutable

class ShootAhead(attackVision: CollisionBody, damage: Float,
                 reloadTime: Int, bulletCreator: BulletCreator) extends UnitElement {

  private var reloadTimer: Int = 0
  private var attackTarget: Option[UnitObject] = None

  override def update(p: GameObject, delta: Int): Unit = {
    val parent = p.asInstanceOf[UnitObject]

    parent.updateCollPolygon(attackVision) //update vision

    //finds a enemy
    if (attackTarget.isEmpty) {
      if (parent.state == UnitObject.State.attack) //marks that unit isn't attacking
        parent.state = UnitObject.State.normal

      val enemy = parent.collHandler.collideCollisionBody(parent, attackVision, null, parent.owner.enemiesAsGameElement)
      enemy.foreach {
        case enemy: UnitObject =>
          attackTarget = Some(enemy)

        case _ => Unit
      }
    }

    //checks shooting and if enemy is still possible target
    attackTarget.foreach(target => {
      if (target.canBeDeleted) attackTarget = None
      else {
        val blockingWall = parent.collHandler.collideLine(parent, parent.pos, target.pos, parent.collHandler.mapFilter)
        if (blockingWall.isDefined) attackTarget = None
        else { //unit can attack
          parent.state = UnitObject.State.attack //marks that unit is attacking
          parent.moveTarget.set(target.pos) //move towards enemy

          if (reloadTimer >= reloadTime) {
            shoot(parent)
            reloadTimer = 0 //reset the timers
          }

        }
      }

    })

    if (MainGame.drawCollBox) attackVision.draw(MainGame.debugRender)

    reloadTimer = math.min(reloadTimer + delta, reloadTime)

  }

  /** Shoots a bullet */
  private def shoot(parent: UnitObject): Unit = {

    //TODO vector pool
    //calculates the pos of the bullet and create it
    val bulletPos = Vector2e(1, 0).setAngle(parent.angle) **
      (parent.sWidth / 2f + bulletCreator.radius) ++ parent.pos
    val bullet = bulletCreator.create(parent, parent.owner.objectHandler,
      bulletPos, Vector2e(1, 0).setAngle(parent.angle) ** (parent.maxSpeed * 5),
      parent.owner.colorIndex)

    //sets the bullet statistics
    bullet.damage = damage
    bullet.collFilter ++= parent.owner.enemies.asInstanceOf[mutable.Buffer[GameElement]]
    bullet.collFilter += parent.collHandler.map
  }

}
