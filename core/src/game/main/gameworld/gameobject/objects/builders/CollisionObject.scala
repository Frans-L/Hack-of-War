package game.main.gameworld.gameobject.objects.builders

import game.GameElement
import game.main.gameworld.collision.bodies.CollisionBody
import game.main.gameworld.gameobject.ObjectHandler
import game.main.gameworld.gameobject.objects.PhysicsObject

object CollisionObject {

  def apply(owner: GameElement,
            collBody: CollisionBody,
            objectHandler: ObjectHandler): PhysicsObject = {

    val (collOriginX, collOriginY) = (collBody.getOriginX, collBody.getOriginY)
    val obj = new PhysicsObject(objectHandler.collHandler, collBody)

    obj.static = true
    obj.origin.set(obj.size.x / 2f, obj.size.y / 2f)
    obj.pos.set(collBody.getX, collBody.getY)
    obj.update()

    //collBody's origin differs from the parent obj
    obj.collBody.setOrigin(collOriginX, collOriginY)
    obj
  }

}
