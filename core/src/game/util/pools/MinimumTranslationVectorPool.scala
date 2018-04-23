package game.util.pools

import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import com.badlogic.gdx.utils.{Pool, Pools}

/**
  * Wrapper to a normal pool to make mtvvectors to support pooling.
  * Vectors wouldn't normally be reset since they doesn't extend Poolable.
  */
object MinimumTranslationVectorPool {

  //pools mtv vectors to reduce garbage collector
  private lazy val pool: Pool[MinimumTranslationVector] =
    Pools.get(classOf[MinimumTranslationVector])

  /** Returns a reset and pooled mtvvector. */
  def obtain(): MinimumTranslationVector = {
    val v = pool.obtain()
    v.normal.set(0, 0)
    v.depth = 0
    v
  }

  def freeAmount: Int = pool.getFree

  def free(v2: MinimumTranslationVector): Unit = pool.free(v2)
}
