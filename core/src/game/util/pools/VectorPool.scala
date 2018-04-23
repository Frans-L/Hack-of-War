package game.util.pools

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.{Pool, Pools}


/**
  * Wrapper to a normal pool to make vectors to support pooling.
  * Vectors wouldn't normally be reset since they doesn't extend Poolable.
  */
object VectorPool {

  //pools vectors to reduce garbage collector
  //Pools.get returns same pool, if same kind pool already exists
  private lazy val pool: Pool[Vector2] = Pools.get(classOf[Vector2])

  def obtain(): Vector2 = pool.obtain().set(0, 0) //have to reset here

  def obtain(v: Vector2): Vector2 = pool.obtain().set(v)

  def obtain(x: Float, y: Float): Vector2 = pool.obtain().set(x, y)

  def free(v: Vector2): Unit = pool.free(v)

  def clear(): Unit = pool.clear()
}
