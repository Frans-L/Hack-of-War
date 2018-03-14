package game.util

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.{Pool, Pools}
import com.badlogic.gdx.utils.Pool.Poolable

/**
  * Created by Frans on 13/03/2018.
  */
object Vector2e {

  //pools vectors to reduce garbage collector
  val pool: Pool[Vector2] = Pools.get(classOf[Vector2])

  def apply(x: Float, y: Float): Vector2 = new Vector2(x, y)

  def pool(x: Float, y: Float): Vector2 = pool.obtain().set(x, y)

  //to make it more scala and poolable
  implicit class Vector2Enhanced(v: Vector2) extends Poolable {

    def width: Float = v.x

    def height: Float = v.y

    def ++(v2: Vector2): Vector2 = v.add(v2)

    def --(v2: Vector2): Vector2 = v.sub(v2)

    def **(i: Float): Vector2 = v.scl(i)

    def **(v2: Vector2): Vector2 = v.scl(v2)

    def */(i: Float): Vector2 = v.scl(1f / i)


    override def reset(): Unit = {
      v.x = 0
      v.y = 0
    }
  }

}