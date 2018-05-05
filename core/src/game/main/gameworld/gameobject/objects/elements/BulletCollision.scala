package game.main.gameworld.gameobject.objects.elements

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import game.main.gameworld.gameobject
import game.main.gameworld.gameobject.{ObjectElement, objects}
import game.util.pools.MinimumTranslationVectorPool

object BulletCollision extends ObjectElement {

  override def update(p: gameobject.GameObject, delta: Int): Unit = {
    val parent = p.asInstanceOf[objects.BulletObject]

    if (!parent.collided) {
      val collForce = MinimumTranslationVectorPool.obtain()
      val crashObj = parent.collHandler.collide(parent, collForce, parent.collFilter)
      crashObj.foreach(o => collision(parent, o, collForce))
      MinimumTranslationVectorPool.free(collForce) //free the memory
    }

  }

  private def collision(bullet: objects.BulletObject,
                        crashObj: objects.PhysicsObject, collForce: MinimumTranslationVector): Unit = {
    crashObj match {
      case obj: objects.UnitObject =>
        obj.reduceHealth(bullet.damage)
        obj.addImpact(bullet.velocity.scl(1f), bullet.mass)
      case _ => Unit
    }

    bullet.delete()
  }

  /** Throws an error if the parent is not valid! */
  override def checkParent(parent: gameobject.GameObject): Unit =
    require(parent.isInstanceOf[objects.BulletObject], "Parent have to be BulletObject!")

}