package test

import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import com.badlogic.gdx.math.{Intersector, Polygon}
import game.loader.GameTextures
import game.main.gameworld.collision.bodies
import game.main.gameworld.gameobject.GameObject
import game.main.gameworld.gameobject.objects.elements.BulletCollision
import game.util.{Ticker, Vector2e}
import org.junit.Test
import org.junit.Assert._


class GeneralTest extends GameTest {

  /** Circle outside of the polygon */
  @Test def polygonOverLapsCircle1() {
    val p = new bodies.PolygonBody(Array(0, 0, 0, 10, 10, 10, 10, 0), 5)
    val c = new bodies.CircleBody(5)
    p.setPosition(0, 0)
    c.setPosition(16, 16)

    assertEquals(false, p.overlaps(c, null))
  }

  /** Circle inside of the polygon */
  @Test def polygonOverLapsCircle2() {
    val p = new bodies.PolygonBody(Array(0, 0, 0, 10, 10, 10, 10, 0), 5)
    val c = new bodies.CircleBody(1)
    val mtv = new MinimumTranslationVector()
    p.setPosition(0, 0)
    c.setPosition(8, 5)

    assertEquals(true, p.overlaps(c, mtv))
    assertEquals(2, mtv.depth, 0.1f)
  }

  /** Polygon inside of a circle */
  @Test def polygonOverLapsCircle3() {
    val p = new bodies.PolygonBody(Array(0, 0, 0, 10, 10, 10, 10, 0), 5)
    val c = new bodies.CircleBody(20)
    val mtv = new MinimumTranslationVector()
    p.setPosition(0, 0)
    c.setPosition(-20, -20)

    assertEquals(true, p.overlaps(c, mtv))
    assertEquals(20, mtv.depth, 0.1f)
  }

  /** Circle intersects Polygon */
  @Test def polygonOverLapsCircle4() {
    val p = new bodies.PolygonBody(Array(0, 0, 0, 10, 10, 10, 10, 0), 5)
    val c = new bodies.CircleBody(2)
    val mtv = new MinimumTranslationVector()
    p.setPosition(0, 0)
    c.setPosition(-2, 3)

    assertEquals(true, p.overlaps(c, mtv))
    assertEquals(2, mtv.depth, 0.1f)
  }

  /** Polygon intersects Polygon */
  @Test def polygonOverLapsPolygon1() {
    val p = new bodies.PolygonBody(Array(0, 0, 0, 10, 10, 10, 10, 0), 5)
    val o = new bodies.PolygonBody(Array(0, 0, 0, 4, 4, 4, 4, 0), 5)
    val mtv = new MinimumTranslationVector()
    p.setPosition(0, 0)
    o.setPosition(-2, 3)

    assertEquals(true, p.overlaps(o, mtv))
    assertEquals(2, mtv.depth, 0.1f)
  }

  /** Polygon intersects Polygon */
  @Test def polygonOverLapsPolygon2() {
    val p = new bodies.PolygonBody(Array(0, 0, 10, 0, 10, 10, 0, 10), 2)
    val o = new bodies.PolygonBody(Array(0, 0, 3, 3 / 2f, 0, 3), 2)

    val mtv = new MinimumTranslationVector()
    val mtv2 = new MinimumTranslationVector()
    //p.setPosition(-3.5f, -2)
    o.setPosition(3.5f, 2)

    assertEquals(true, o.overlaps(p, mtv))
    assertEquals(5, mtv.depth, 0.1f)

  }

  /** Should be able to chain objects */
  @Test def gameObjectElementTest() {

    val a = new GameObject()
    val b = new GameObject()
    val c = new GameObject()
    b.appendElement(c)
    a.prependElement(b)
    a.pos.set(5, 5)
    b.pos.set(2, 2)
    c.pos.set(1, 1)
    a.update()

    assertEquals(c.pos.x, 1, 0.01f)
    assertEquals(c.pos.y, 1, 0.01f)


  }

  /** Shouldn't be able to chain wrong elements. */
  @Test def gameObjectElementTest2() {

    val a = new GameObject()
    val b = new GameObject()
    val c = new GameObject()
    b.appendElement(c)
    a.prependElement(b)
    a.update()

    try {
      c.appendElement(BulletCollision)
      a.update()
      assert(false)
    } catch {
      case e: IllegalArgumentException => assert(true)
      case _: Throwable => assert(false)
    }


  }

  /** Should work with implicit methods */
  @Test def vector2e(): Unit = {
    import game.util.Vector2e._

    val v = Vector2e(10, 10)
    v ** 10 / 2 -- Vector2e(30, 25)

    assertEquals(v.width, 20, 0.01f)
    assertEquals(v.height, 25, 0.01f)
  }

}
