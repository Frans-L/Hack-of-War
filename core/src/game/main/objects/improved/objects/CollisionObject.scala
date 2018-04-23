package game.main.objects.improved.objects

import game.GameElement
import game.main.objects.improved.{GameObject, PhysicsElement}
import game.main.physics.PhysicsWorld
import game.main.physics.collision.CollisionBody

object CollisionObject {

  def apply(owner: GameElement,
            collBody: CollisionBody,
            physWorld: PhysicsWorld): GameObject = {

    val obj = new GameObject()

    obj.origin.set(obj.size.x / 2f, obj.size.y / 2f)
    obj.pos.set(collBody.getX, collBody.getY)

    val phys = new PhysicsElement(physWorld, collBody)
    physWorld.addUnit(owner, phys)
    obj.addElement(phys)
    obj
  }
}
