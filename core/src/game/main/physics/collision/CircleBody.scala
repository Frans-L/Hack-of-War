package game.main.physics.collision

import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import com.badlogic.gdx.math.{Circle, Vector2}
import game.util.Vector2e

class CircleBody(private var radius: Float) extends CollisionBody {

  private var scaleX: Float = 1f
  private var scaleY: Float = 1f
  private var x: Float = 0f
  private var y: Float = 0f

  override protected val centerVector: Vector2 = Vector2e(radius, radius)


  /** Every CollisionBody should have a radius for a fast circle collision check */
  override def setRadius(r: Float): Unit = radius = r

  override def getRadius: Float = radius

  override def getRadiusScaled: Float = radius * math.max(getScaleX, getScaleY)


  /** Position is always from left down corner
    * because of the limitations of the gdx.math.Polygon. */
  override def setPosition(x: Float, y: Float): Unit = {
    this.x = x
    this.y = y
  }

  override def getX: Float = x

  override def getY: Float = y


  /** The body should be scaled without changing the original size. */
  override def setScale(x: Float, y: Float): Unit = {
    scaleX = x
    scaleY = y
  }

  override def getScaleX: Float = scaleX

  override def getScaleY: Float = scaleY


  /** Circle angle doesn't do anything */
  override def setRotation(angle: Float): Unit = Unit

  override def getRotation: Float = 0f


  /** The position of the origin from the left down corner. */
  override def setOrigin(x: Float, y: Float): Unit = centerVector.set(x, y)

  override def getOriginX: Float = centerVector.x

  override def getOriginY: Float = centerVector.y


  /** Returns true if overlaps, and sets the mtv vector. */
  override def overlapsPolygon(polygon: PolygonBody, mtv: MinimumTranslationVector): Boolean = {
    val result = polygon.overlapsCircle(centerVector, getRadiusScaled, mtv)

    //mtv vectors points to wrong direction
    // => idea is to move circle this time
    if (result) mtv.normal.scl(-1)
    result
  }


  /** Returns tuple(isCollided, angle)
    * isCollided: Boolean => True if collided
    * angle: Float => Collision angle in degrees */
  override def overlapsCircle(center: Vector2, r: Float,
                              mtv: MinimumTranslationVector): Boolean = {

    val radiusS = getRadiusScaled
    val isCollided = centerVector.dst2(center) <= (r + radiusS) * (r + radiusS)

    //sets the mtv vector if collided
    if (isCollided) {
      mtv.normal.set(
        center.x - centerVector.x,
        center.y - centerVector.y)
        .nor()
      mtv.depth = r + radiusS - centerVector.dst(center)
    }

    isCollided
  }

  /** Returns the Center vector and the radius */
  override def toCircle: (Vector2, Float) = (centerVector, getRadiusScaled)


  /** Returns true if the point is inside the body */
  override def contains(x: Float, y: Float): Boolean =
    centerVector.dst2(x, y) <= getRadiusScaled * getRadiusScaled

  override def contains(vector: Vector2): Boolean =
    centerVector.dst2(center) <= getRadiusScaled * getRadiusScaled
}
