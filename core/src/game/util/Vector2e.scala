package game.util

import com.badlogic.gdx.math.Vector2

/**
  * Created by Frans on 13/03/2018.
  */
object Vector2e {

  def apply(x: Float, y: Float): Vector2 = new Vector2(x, y)


  //to make it more scala
  implicit class Vector2Enhanced(v: Vector2) {

    def width: Float = v.x

    def height: Float = v.y

    def ++(v2: Vector2): Vector2 = v.add(v2)

    def --(v2: Vector2): Vector2 = v.sub(v2)

    def **(i: Float): Vector2 = v.scl(i)

    def **(v2: Vector2): Vector2 = v.scl(v2)

    def */(i: Float): Vector2 = v.scl(1f / i)

  }

}