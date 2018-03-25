package game.main.physics.objects

import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import game.main.physics.CollisionHandler
import game.util.Vector2e._

/**
  * Created by Frans on 06/03/2018.
  */
class BorderSprite(val pos: Vector2, val size: Vector2,
                   val padding: Vector2, var sprite: Sprite) extends SpriteObject {

  updateSprite()

  override def update(): Unit = ???

  override def draw(shapeRender: ShapeRenderer): Unit = ???

  override def draw(batch: Batch): Unit = {
    if (visible) {
      sprite.draw(batch)
    }
  }

  override def updateSprite(): Unit = {
    super.updateSprite()

    //set bo
    sprite.setBounds(
      pos.x - padding.x, pos.y - padding.y,
      size.width - padding.x * 2f, size.y - padding.y * 2f)
    sprite.setOriginCenter()
  }
}
