package game.main.physics.objects
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import game.main.physics.collision.PolygonBody

abstract class BulletObject extends ObjectType {

  /*
  override val collBody: CollisionBody = _
  override val velocity: Vector2 = _
  override val mass: Float = _
  override val pos: Vector2 = _
  override val size: Vector2 = _
  override var sprite: Sprite = _
  */

  override def update(): Unit = ???

  override def draw(shapeRender: ShapeRenderer): Unit = ???

  override def draw(batch: Batch): Unit = ???
}
