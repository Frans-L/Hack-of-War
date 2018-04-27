package game.main.gameobject.objects

import com.badlogic.gdx.Gdx
import game.main.physics.CollisionHandler
import game.main.physics.collision.CollisionBody

class BulletObject(physWorld: CollisionHandler, collBody: CollisionBody)
  extends PhysicsObject(physWorld, collBody) {

  //bullet only needs damage, lol
  var damage: Float = 20f

}
