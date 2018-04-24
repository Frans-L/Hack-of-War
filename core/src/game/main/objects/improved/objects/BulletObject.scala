package game.main.objects.improved.objects

import game.main.objects.improved.PhysicsObject
import game.main.physics.CollisionHandler
import game.main.physics.collision.CollisionBody

class BulletObject(physWorld: CollisionHandler, collBody: CollisionBody)
  extends PhysicsObject(physWorld, collBody) {

  //bullet only needs damage, lol
  var damage: Float = 20f

}
