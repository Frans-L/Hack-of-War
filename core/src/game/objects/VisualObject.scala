package game.objects

import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.{Polygon, Rectangle, Shape2D}

/**
  * Created by Frans on 06/03/2018.
  */
class VisualObject(var posX: Float, var posY: Float,
                   var width: Float, var height: Float,
                   var paddingW: Float, var paddingH: Float,
                   var sprite: Sprite) extends GameObject {

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
      posX - paddingW, posY - paddingH,
      width - paddingW * 2, height - paddingH * 2f)
    sprite.setOriginCenter()
  }
}
