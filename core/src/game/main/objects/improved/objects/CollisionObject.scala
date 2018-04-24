package game.main.objects.improved.objects

import game.GameElement
import game.main.objects.improved.ObjectHandler.Level
import game.main.objects.improved.{ObjectHandler, PhysicsElement, PhysicsObject}
import game.main.physics.CollisionHandler
import game.main.physics.collision.CollisionBody

object CollisionObject {

  def apply(owner: GameElement,
            collBody: CollisionBody,
            objectHandler: ObjectHandler): PhysicsObject = {

    val obj = new PhysicsObject(objectHandler.collHandler, collBody)
    obj.origin.set(obj.size.x / 2f, obj.size.y / 2f)
    obj.pos.set(collBody.getX, collBody.getY)
    obj.update()

    objectHandler.addObject(obj, Level.ground, false, false, true, owner)

    obj
  }
}
