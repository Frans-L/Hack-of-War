package game.main.objects.improved.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.main.objects.improved.{GameObject, PhysicsObject}
import game.main.physics.CollisionHandler
import game.main.physics.collision.CollisionBody

class BulletObject(physWorld: CollisionHandler, collBody: CollisionBody)
  extends PhysicsObject(physWorld, collBody) {

  //bullet only needs damage, lol
  var damage: Float = 20f

}
