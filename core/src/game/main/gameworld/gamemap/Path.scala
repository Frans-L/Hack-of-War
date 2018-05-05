package game.main.gameworld.gamemap

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite, SpriteBatch}
import com.badlogic.gdx.math.{Intersector, MathUtils, Vector2}
import game.util.Vector2e
import game.util.Vector2e._

/**
  * Checkpoints of the route.
  */
class Path(private var route: Seq[Vector2], var maxOffset: Float) {

  //make sure that path always exists
  if (route == null || route.size <= 1) route = Seq(Vector2e(0, 0), Vector2e(0, 0))

  //how many points from the start are ignored when calculating offset
  val startPathLength = 2

  def head: Vector2 = route.head

  def length: Int = route.length

  def apply(i: Int): Vector2 = route(i)

  /** Returns itself because of chaining. */
  def reverse: Path = {
    route = route.reverse
    this
  }

  //caches used vector
  private val tmpVec: Vector2 = Vector2e(0, 0)

  /** Returns the direction where to move from 'i' checkpoint. */
  def direction(i: Int): Vector2 = tmpVec.set(route(math.min(i + 1, length - 1))).sub(route(i))


  /** Changes the current path by offset. Returns itself to make chaining possible. */
  def setOffset(offset: Float): Path = {
    val off = limitOffset(offset)
    for (i <- startPathLength until route.length) {
      route(i) ++ direction(i).nor.rotate90(1).scl(off)
    }
    this
  }

  /** Returns the copy of the path. */
  def copy: Path = {
    val newRoute: Array[Vector2] = new Array[Vector2](route.length)
    for (i <- route.indices) newRoute(i) = route(i).cpy()
    new Path(newRoute, maxOffset)
  }

  /** Returns itself for the chaining. */
  def changeRoute(r: Seq[Vector2]): Path = {
    //tries to avoid sending the whole route to the garbage collector
    if (r.length == route.length)
      for (i <- route.indices) route(i).set(r(i))
    else { //if have to, create a new route
      val newRoute: Array[Vector2] = new Array[Vector2](r.length)
      for (i <- r.indices) newRoute(i) = r(i).cpy()
      route = newRoute
    }
    this
  }

  /** Finds and returns the offset from the given position. */
  def findOffset(x: Float, y: Float): Float = {
    var offset = Float.MaxValue
    var index = 0
    for (i <- 0 until route.length - 1) {
      val off = Intersector.distanceSegmentPoint(route(i).x, route(i).y,
        route(i + 1).x, route(i + 1).y, x, y)

      //saves the lowest value
      if (off <= offset) {
        index = i
        offset = off
      }
    }

    var dir = Intersector.pointLineSide(route(index).x, route(index).y,
      route(index + 1).x, route(index + 1).y, x, y)

    math.min(offset, maxOffset) * dir
  }

  /** Returns a offset that is limited to be under maxOffset. */
  def limitOffset(off: Float): Float = math.min(math.max(-maxOffset, off), maxOffset)

  /** Returns a random spot between i and i+1 route's points. */
  def randomSpot(i: Int): Vector2 = direction(i).scl(MathUtils.random(1f)).add(route(i))

  /** Returns a random offset */
  def randomOffset: Float = MathUtils.random(-maxOffset, maxOffset)

  /** Returns the route */
  def getRoute: Seq[Vector2] = route

  /** Draws all points of the route */
  def draw(batch: Batch, icon: Sprite, gap: Float, startGap: Float): Unit = {

    var leftOver: Float = startGap
    for (i <- route.indices) {
      leftOver = drawBetween(batch, icon, i, gap, leftOver)
    }

  }

  /** Draws the sprites between two route's points */
  private def drawBetween(batch: Batch, sprite: Sprite, i: Int, gap: Float, startGap: Float): Float = {

    val dist = route(i).dst(route(math.min(i + 1, route.length - 1)))
    val dir = direction(i).nor //direction to look at
    val startPos = route(i)
    val middleX = sprite.getWidth * sprite.getScaleX / 2f //sprites center
    val middleY = sprite.getHeight * sprite.getScaleY / 2f
    var pos = startGap

    while (pos <= dist) {
      sprite.setPosition(startPos.x + dir.x * pos - middleX, startPos.y + dir.y * pos - middleY)
      sprite.setRotation(dir.angle)
      sprite.draw(batch)
      pos += gap
    }

    pos - dist //returns the amount that went over

  }

}
