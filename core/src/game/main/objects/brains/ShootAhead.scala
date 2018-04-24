package game.main.objects.brains

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.GameElement
import game.main.MainGame
import game.main.objects.improved.{GameObject, ObjectElement, UnitObject}
import game.main.physics.collision.CollisionBody
import game.util.Vector2e
import game.util.Vector2e._

import scala.collection.mutable

class ShootAhead(attackVision: CollisionBody, reloadTime: Int) extends ObjectElement {

  private var reloadTimer: Int = 0
  private var attackTarget: Option[UnitObject] = None

  override def update(p: GameObject, delta: Int): Unit = {
    val parent = p.asInstanceOf[UnitObject]

    parent.updateCollPolygon(attackVision) //update vision

    //finds a enemy
    if (attackTarget.isEmpty) {
      val enemy = parent.physWorld.collideCollisionBody(parent, attackVision, null, parent.owner.enemiesAsGameElement)
      enemy.foreach {
        case enemy: UnitObject => attackTarget = Some(enemy)
        case _ => Unit
      }
    }

    //checks shooting and if enemy is still possible target
    attackTarget.foreach(target => {
      if (target.canBeDeleted) attackTarget = None
      else {

        parent.moveTarget.set(target.pos)
        val blockingWall = parent.physWorld.collideLine(parent, parent.pos, target.pos, parent.physWorld.mapFilter)

        if (blockingWall.isDefined) attackTarget = None
        else if (reloadTimer >= reloadTime) {
          shoot(parent)
          reloadTimer = 0 //reset the timers
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
    val bulletPos =
      Vector2e(parent.movingForce).nor ** (parent.sWidth / 2f + parent.bulletCreator.radius) ++ parent.pos
    val bullet = parent.bulletCreator.create(parent, parent.owner.objectHandler,
      bulletPos, Vector2e(parent.movingForce).nor ** (parent.maxSpeed * 5),
      parent.owner.colorIndex)

    //sets the bullet statistics
    bullet.damage = parent.damage
    bullet.collFilter ++= parent.owner.enemies.asInstanceOf[mutable.Buffer[GameElement]]
    bullet.collFilter += parent.physWorld.map
  }

  /** Throws an error if the parent is not valid! */
  override def checkParent(parent: GameObject): Unit =
    require(parent.isInstanceOf[UnitObject], "Parent have to be UnitObject")
}
