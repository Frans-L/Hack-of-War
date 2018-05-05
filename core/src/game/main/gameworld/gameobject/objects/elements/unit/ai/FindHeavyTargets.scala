package game.main.gameworld.gameobject.objects.elements.unit.ai

import com.badlogic.gdx.Gdx
import game.main.gameworld.collision.bodies.CollisionBody
import game.main.gameworld.gameobject.GameObject
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.gameworld.gameobject.objects.elements.unit.UnitElement
import game.util.Vector2e._

class FindHeavyTargets(findRange: Float) extends UnitElement {

  private var range2: Float = findRange * findRange
  private var target: Option[UnitObject] = None

  override def update(p: GameObject, delta: Int): Unit = {
    val parent = p.asInstanceOf[UnitObject]

    //checks each enemies' object, and finds the building
    if (target.isEmpty) {
      parent.owner.enemies.foreach(enemy => {
        parent.collHandler.getPhysicsObjects(enemy).foreach(obj => {
          if (target.isEmpty) {
            if (obj.isInstanceOf[GameObject]) {
              val o = obj.asInstanceOf[UnitObject]
              if ((o.category == UnitObject.Category.building || o.category == UnitObject.Category.tank)
                && parent.pos.dst2(o.pos) <= range2) {
                target = Some(o)
              }
            }
          }
        })
      })
    }

    //if target exists, stop the object
    target.foreach(obj => {
      if (!obj.canBeDeleted && parent.pos.dst2(obj.pos) <= range2) {
        parent.movingForce ** 0
      } else target = None
    })

  }

}
