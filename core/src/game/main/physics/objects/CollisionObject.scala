package game.main.physics.objects

import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import game.main.physics.CollisionBody
import game.util.Vector2e

class CollisionObject(val collBody: CollisionBody) extends ObjectType {

  override val velocity: Vector2 = Vector2e(0, 0)
  override val mass: Float = 100f
  override val pos: Vector2 = Vector2e(collBody.getX, collBody.getY)
  override val size: Vector2 = Vector2e(collBody.getRadius, collBody.getRadius)
  override var sprite: Sprite = _
  override val origin: Vector2 = Vector2e(size.x / 2f, size.y / 2f)

  override def update(): Unit = Unit //doesn't move anywhere

  override def draw(shapeRender: ShapeRenderer): Unit = Unit //doesn't draw anything

  override def draw(batch: Batch): Unit = Unit //doesn't draw anything
}
