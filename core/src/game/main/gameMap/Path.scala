package game.main.gameMap

import com.badlogic.gdx.math.Vector2

/**
  * Checkpoints of the route.
  */
class Path(val route: Seq[Vector2], val width: Float, val height: Float) extends Seq[Vector2] {

  override def length: Int = route.length

  override def apply(idx: Int): Vector2 = route(idx)

  override def iterator: Iterator[Vector2] = new Iterator[Vector2] {
    private var i: Int = 0

    override def hasNext: Boolean = i < length

    override def next(): Vector2 = {
      i += 1
      route(i - 1)
    }
  }
}
