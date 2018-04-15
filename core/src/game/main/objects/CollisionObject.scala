package game.main.objects

import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import game.GameElement
import game.main.physics.{ObjectType, PhysicsWorld}
import game.main.physics.collision.PolygonBody
import game.util.Vector2e

class CollisionObject(var owner: GameElement,
                      override val physWorld: PhysicsWorld,
                      override val collBody: PolygonBody) extends ObjectType {

  override val velocity: Vector2 = Vector2e(0, 0)
  override var mass: Float = 100f
  override var friction: Float = 10f
  override val pos: Vector2 = Vector2e(collBody.getX, collBody.getY)
  override val size: Vector2 = Vector2e(collBody.getRadius, collBody.getRadius)
  override var sprite: Sprite = _
  override val origin: Vector2 = Vector2e(size.x / 2f, size.y / 2f)

  collToOthers = false //doesn't check collision with others by itself

  physWorld.addUnit(owner, this) //add to physics world



  override def updatePhysics(): Unit = {
    velocity.set(0,0) //doesn't move anything
  }

  override def update(): Unit = Unit //doesn't move anywhere

  override def draw(shapeRender: ShapeRenderer): Unit = Unit //doesn't draw anything

  override def draw(batch: Batch): Unit = Unit //doesn't draw anything

}
