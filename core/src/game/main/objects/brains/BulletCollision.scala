package game.main.objects.brains

import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import game.main.objects.CollisionObject
import game.main.objects.improved.objects.BulletObject
import game.main.objects.improved.{GameObject, ObjectElement, PhysicsObject, UnitObject}
import game.util.pools.MinimumTranslationVectorPool
import gdxfixed.math.Intersector

object BulletCollision extends ObjectElement {

  override def update(p: GameObject, delta: Int): Unit = {
    val parent = p.asInstanceOf[BulletObject]

    if (!parent.collided) {
      val collForce = MinimumTranslationVectorPool.obtain()
      val crashObj = parent.physWorld.collide(parent, collForce, parent.collFilter)
      crashObj.foreach(o => collision(parent, o, collForce))
      MinimumTranslationVectorPool.free(collForce) //free the memory
    }

  }

  private def collision(bullet: BulletObject,
                        crashObj: PhysicsObject, collForce: MinimumTranslationVector): Unit = {
    crashObj match {
      case obj: UnitObject =>
        obj.reduceHealth(bullet.damage)
        obj.addImpact(bullet.velocity.scl(1f), bullet.mass)
      case _ => Unit
    }
    bullet.delete()
  }

  /** Throws an error if the parent is not valid! */
  override def checkParent(parent: GameObject): Unit =
    require(parent.isInstanceOf[BulletObject], "Parent have to be BulletObject!")

}