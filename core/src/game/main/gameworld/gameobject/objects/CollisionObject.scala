package game.main.gameworld.gameobject.objects

import game.GameElement
import game.main.gameworld.collision.bodies.CollisionBody
import game.main.gameworld.gameobject
import game.main.gameworld.gameobject.ObjectHandler
import game.main.gameworld.gameobject.ObjectHandler.Level

object CollisionObject {

  def apply(owner: GameElement,
            collBody: CollisionBody,
            objectHandler: ObjectHandler): PhysicsObject = {

    val (collOriginX, collOriginY) = (collBody.getOriginX, collBody.getOriginY)
    val obj = new PhysicsObject(objectHandler.collHandler, collBody)
    obj.origin.set(obj.size.x / 2f, obj.size.y / 2f)
    obj.pos.set(collBody.getX, collBody.getY)
    obj.update()
    obj.collBody.setOrigin(collOriginX, collOriginY)

    objectHandler.addObject(obj, Level.ground, update = false, draw = true, collision = true, owner)

    obj
  }
}
