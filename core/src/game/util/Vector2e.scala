package game.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.{Pool, Pools}
import com.badlogic.gdx.utils.Pool.Poolable

/**
  * Created by Frans on 13/03/2018.
  */
object Vector2e {

  //pools vectors to reduce garbage collector
  private val poolV: Pool[Vector2] = Pools.get(classOf[Vector2])

  //Creates a new vector
  def apply(x: Float, y: Float): Vector2 = new Vector2(x, y)

  def apply(v: Vector2): Vector2 = new Vector2(v)

  def freeAmount: Int = poolV.getFree

  def free(v2: Vector2): Unit = poolV.free(v2)

  //Returns a vector that is pooled
  def pool(x: Float, y: Float): Vector2 = poolV.obtain().set(x, y)

  def pool(v2: Vector2): Vector2 = poolV.obtain().set(v2)

  def pool(): Vector2 = poolV.obtain().set(0, 0) //reset happens at this moment

  //To make it more scala
  implicit class Vector2Enhanced(v: Vector2) {

    def width: Float = v.x

    def height: Float = v.y

    def ++(v2: Vector2): Vector2 = v.add(v2)

    def --(v2: Vector2): Vector2 = v.sub(v2)

    def **(i: Float): Vector2 = v.scl(i)

    def **(v2: Vector2): Vector2 = v.scl(v2)

    def */(i: Float): Vector2 = v.scl(1f / i)

    def clampAngle(min: Float, max: Float): Unit = {
      val a = v.angle()
      if (a < min % 360 - 10){
        v.setAngle(min)
        Gdx.app.log("Vector", "!! MIN !! " + a + " " + min % 360)
      }
      else if (a > max % 361 + 10) {
        v.setAngle(max)
        Gdx.app.log("Vector", "!! MAX !!"+ a + " " + max % 361)
      }
    }

  }

}