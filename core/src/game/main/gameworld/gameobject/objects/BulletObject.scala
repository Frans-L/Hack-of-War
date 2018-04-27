package game.main.gameworld.gameobject.objects

import com.badlogic.gdx.Gdx
import game.main.gameworld.collision.CollisionHandler
import game.main.gameworld.collision.bodies.CollisionBody

class BulletObject(physWorld: CollisionHandler, collBody: CollisionBody)
  extends PhysicsObject(physWorld, collBody) {

  //bullet only needs damage, lol
  var damage: Float = 20f

}
