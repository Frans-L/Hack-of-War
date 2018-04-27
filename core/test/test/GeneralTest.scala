package test

import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import com.badlogic.gdx.math.{Intersector, Polygon}
import org.junit.Test
import org.junit.Assert._
import game.main.physics.collision._


class GeneralTest extends GameTest {

  /** Circle outside of the polygon */
  @Test def polygonOverLapsCircle1() {
    val p = new PolygonBody(Array(0, 0, 0, 10, 10, 10, 10, 0), 5)
    val c = new CircleBody(5)
    p.setPosition(0, 0)
    c.setPosition(16, 16)

    assertEquals(false, p.overlaps(c, null))
  }

  /** Circle inside of the polygon */
  @Test def polygonOverLapsCircle2() {
    val p = new PolygonBody(Array(0, 0, 0, 10, 10, 10, 10, 0), 5)
    val c = new CircleBody(1)
    val mtv = new MinimumTranslationVector()
    p.setPosition(0, 0)
    c.setPosition(8, 5)

    assertEquals(true, p.overlaps(c, mtv))
    assertEquals(2, mtv.depth, 0.1f)
  }

  /** Polygon inside of a circle */
  @Test def polygonOverLapsCircle3() {
    val p = new PolygonBody(Array(0, 0, 0, 10, 10, 10, 10, 0), 5)
    val c = new CircleBody(20)
    val mtv = new MinimumTranslationVector()
    p.setPosition(0, 0)
    c.setPosition(-20, -20)

    assertEquals(true, p.overlaps(c, mtv))
    assertEquals(20, mtv.depth, 0.1f)
  }

  /** Circle intersects Polygon */
  @Test def polygonOverLapsCircle4() {
    val p = new PolygonBody(Array(0, 0, 0, 10, 10, 10, 10, 0), 5)
    val c = new CircleBody(2)
    val mtv = new MinimumTranslationVector()
    p.setPosition(0, 0)
    c.setPosition(-2, 3)

    assertEquals(true, p.overlaps(c, mtv))
    assertEquals(2, mtv.depth, 0.1f)
  }

  /** Polygon intersects Polygon */
  @Test def polygonOverLapsPolygon1() {
    val p = new PolygonBody(Array(0, 0, 0, 10, 10, 10, 10, 0), 5)
    val o = new PolygonBody(Array(0, 0, 0, 4, 4, 4, 4, 0), 5)
    val mtv = new MinimumTranslationVector()
    p.setPosition(0, 0)
    o.setPosition(-2, 3)

    assertEquals(true, p.overlaps(o, mtv))
    assertEquals(2, mtv.depth, 0.1f)
  }

  /** Polygon intersects Polygon */
  @Test def polygonOverLapsPolygon2() {
    val p = new PolygonBody(Array(0, 0, 10, 0, 10, 10, 0, 10), 2)
    val o = new PolygonBody(Array(0, 0, 3, 3/2f, 0, 3), 2)

    val mtv = new MinimumTranslationVector()
    val mtv2 = new MinimumTranslationVector()
    //p.setPosition(-3.5f, -2)
    o.setPosition(3.5f, 2)

    assertEquals(true, o.overlaps(p, mtv))
    assertEquals(5, mtv.depth, 0.1f)

  }

}
