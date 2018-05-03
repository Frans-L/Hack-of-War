package game.main.gameworld.collision.bodies

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import com.badlogic.gdx.math.{Intersector, Vector2}
import game.util.Vector2e


object CollisionBody {

  lazy val tmp1 = Vector2e(0, 0)
  lazy val tmp2 = Vector2e(0, 0)

  /** Returns true if the segment circle collided and sets the MinimumTranslationVector to
    * tell how much and in which direction the circle have to move to avoid collision.
    *
    * @return true if collided
    */
  def intersectSegmentCircle(start: Vector2, end: Vector2, center: Vector2, radius: Float,
                             mtv: MinimumTranslationVector): Boolean = {

    tmp1.set(0, 0) //reset tmp vectors
    tmp2.set(center)

    Intersector.nearestSegmentPoint(start, end, center, tmp1)


    if (tmp2.dst2(tmp1) <= radius * radius) {
      if (mtv != null) {
        mtv.depth = math.abs(tmp2.dst(tmp1) - radius)
        mtv.normal = mtv.normal.set(tmp2.sub(tmp1).nor()).scl(-1)
      }
      true
    } else
      false
  }

}

/** *
  * Wrapper for different shape like circle and polygon.
  * Note: Setters and getters to make it same style with libgdx java library.
  */
trait CollisionBody {

  //caches the center point vector aka tmp vector
  protected val centerVector: Vector2 = Vector2e(0, 0)

  /** Every CollisionBody should have a radius for a fast circle collision check */
  def setRadius(r: Float)

  def getRadius: Float

  def getRadiusScaled: Float


  /** Center vector which is */
  def center: Vector2 = centerVector.set(getX + getOriginX, getY + getOriginY)

  /** Position is always from left down corner
    * because of the limitations of the gdx.math.Polygon. */
  def setPosition(x: Float, y: Float)

  def getX: Float

  def getY: Float


  /** The body should be scaled without changing the original size */
  def setScale(x: Float, y: Float)

  def getScaleX: Float

  def getScaleY: Float


  /** Angle is in degrees */
  def setRotation(angle: Float)

  def getRotation: Float


  /** The position of the origin from the left down corner. */
  def setOrigin(x: Float, y: Float)

  def getOriginX: Float

  def getOriginY: Float


  /** Returns true if the point is inside the body */
  def contains(x: Float, y: Float): Boolean

  def contains(vector: Vector2): Boolean

  /** Returns true if overlaps, and sets the mtv vector.
    * mtv tells in which direction and how much the object
    * have to move to avoid the collision. This method takes
    * into account whether the collisionBody is invisible or not. */
  def overlaps(body: CollisionBody, mtv: Intersector.MinimumTranslationVector): Boolean = {
    body match {
      case b: PolygonBody =>
        overlapsPolygon(b, mtv)
      case b: CircleBody =>
        overlapsCircle(b.center, b.getRadiusScaled, mtv)
      case _ => throw new RuntimeException("Not found type of: " + body)
    }
  }


  /** Returns true if overlaps, and sets the mtv vector. */
  def overlapsCircle(center: Vector2, radius: Float, mtv: MinimumTranslationVector): Boolean

  /** Returns true if overlaps, and sets the mtv vector. */
  def overlapsPolygon(polygon: PolygonBody, mtv: MinimumTranslationVector): Boolean

  /** Returns true if overlaps a line. */
  def overlapsLine(startPos: Vector2, endPos: Vector2): Boolean

  /** Returns the Center vector and the radius */
  def toCircle: (Vector2, Float)

  /** Draws the collisionBody */
  def draw(shapeRender: ShapeRenderer): Unit

}
