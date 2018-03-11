package game.objects

import com.badlogic.gdx.graphics.g2d.{Batch, Sprite, TextureRegion}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.Ticker

/**
  * Created by Frans on 26/02/2018.
  */
class ActiveObject(ticker: Ticker, sprite: Sprite, var posX: Float, var posY: Float) extends GameObject {

  override def update(): Unit = {
    if (enabled) {

      posX += 0.06f * ticker.delta

      /*
      Gdx.app.setLogLevel(Application.LOG_DEBUG)
      Gdx.app.log("tmp", "posX: " + posX)
      Gdx.app.log("tmp", "Ticker: " + ticker.elapsed)
      */

      sprite.setPosition(posX, posY)
    }
  }

  def setSize(w: Float, h: Float): Unit = {
    sprite.setSize(w, h)
    sprite.setOriginCenter()
  }

  override def draw(shapeRender: ShapeRenderer): Unit = {
    if (visible) {
      shapeRender.circle(sprite.getX.toInt, sprite.getY.toInt, 30)
    }
  }

  override def draw(batch: Batch): Unit = {
    if (visible) {
      sprite.draw(batch)
    }
  }


}

