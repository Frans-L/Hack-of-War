package game.main.objects

import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import game.GameElement
import game.main.MainGame
import game.main.physics.{ObjectType, CollisionHandler}
import game.main.physics.collision.{CollisionBody, PolygonBody}
import game.util.Vector2e

class CollisionObject(var owner: GameElement,
                      override var physWorld: CollisionHandler,
                      override var collBody: CollisionBody) extends ObjectType {

  pos.set(collBody.getX, collBody.getY)
  size.set(collBody.getRadius, collBody.getRadius)
  origin.set(size.x / 2f, size.y / 2f)
  override var sprite: Sprite = _

  collToOthers = false //doesn't check collision with others by itself

 // physWorld.addUnit(owner, this) //add to physics world


  override def updatePhysics(): Unit = {
    velocity.set(0, 0) //doesn't move anything
  }

  override def update(): Unit = Unit //doesn't move anywhere

  override def draw(shapeRender: ShapeRenderer): Unit = {
    if (MainGame.drawCollBox) {
      collBody.draw(shapeRender) //draw debug lines
    }
  }

  override def draw(batch: Batch): Unit = Unit //doesn't draw anything

  override def reset(): Unit = ???

}
