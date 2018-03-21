package game.util

import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.{Pool, Pools}
import game.util.Vector2e.poolV

/**
  * Created by Frans on 21/03/2018.
  */
object Vector2mtv {

  //pools mtv vectors to reduce garbage collector
  private val poolmtvV: Pool[MinimumTranslationVector] =
    Pools.get(classOf[MinimumTranslationVector])

  /**
    * Returns a reset and pooled mtvvector.
    *
    * @return a minimumTranslationVector
    */
  def pool(): MinimumTranslationVector = {
    val v = poolmtvV.obtain()
    v.normal.set(0, 0)
    v.depth = 0
    v
  }

  def freeAmount: Int = poolmtvV.getFree

  def free(v2: MinimumTranslationVector): Unit = poolmtvV.free(v2)
}
