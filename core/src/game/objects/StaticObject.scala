package game.objects

import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
  * Created by Frans on 06/03/2018.
  */
class StaticObject(posX: Float, posY: Float,
                   width: Float, height: Float,
                   borderW: Float, borderH: Float,
                   sprite: Sprite) extends GameObject {

  sprite.setSize(width - borderW * 2, height - borderH * 2f)
  sprite.setOriginCenter()
  sprite.setPosition(posX - borderW, posY - borderH)

  //Get location next to this object
  def nextToX: Float = this.posX + this.width

  override def update(): Unit = ???

  override def draw(shapeRender: ShapeRenderer): Unit = ???

  override def draw(batch: Batch): Unit = {

    if (visible) {
      sprite.draw(batch)
    }

  }
}
