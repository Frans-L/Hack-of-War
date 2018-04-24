package game.main.objects.brains

import game.main.objects.improved.{GameObject, ObjectElement, PhysicsObject}
import game.util.pools.MinimumTranslationVectorPool

object BounceCollision extends ObjectElement {

  override def update(p: GameObject, delta: Int): Unit = {
    require(p.isInstanceOf[PhysicsObject], "Parent have to be PhysicsObject")
    val parent = p.asInstanceOf[PhysicsObject]

    if (!parent.collided) {
      val collForce = MinimumTranslationVectorPool.obtain()
      val crashObj = parent.physWorld.collide(parent, collForce, parent.collFilter)
      crashObj.foreach(obj => {
        parent.collision(obj, collForce.normal, collForce.depth / 2f)
        obj.collision(obj, collForce.normal.scl(-1), collForce.depth / 2f)
      })
      MinimumTranslationVectorPool.free(collForce) //free the memory
    }

  }

  /** Throws an error if the parent is not valid! */
  override def checkParent(parent: GameObject): Unit =
    require(parent.isInstanceOf[PhysicsObject], "Parent have to be UnitObject")

}