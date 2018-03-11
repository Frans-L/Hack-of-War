package game.objects

import com.badlogic.gdx.graphics.g2d.{Batch, Sprite, TextureRegion}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.{Ticker, Utils}
import game.main.CollisionDetector

/**
  * Created by Frans on 26/02/2018.
  */
class ActiveObject(ticker: Ticker, sprite: Sprite, collDetect: CollisionDetector,
                   x: Float, y: Float, w: Float, h: Float) extends GameObject {

  var removed: Boolean = false

  sprite.setSize(w, h)
  sprite.setOriginCenter()
  var body = Utils.rectanglePolygon(x, y, w, h)

  collDetect.addShape(body)

  override def update(): Unit = {
    if (enabled) {


      if (!collDetect.collide(body) && body.getX < 1920/2f)
        body.setPosition(body.getX + 0.1f * ticker.delta, body.getY)
      else
        destroy()


      /*
      Gdx.app.setLogLevel(Application.LOG_DEBUG)
      Gdx.app.log("tmp", "posX: " + posX)
      Gdx.app.log("tmp", "Ticker: " + ticker.elapsed)
      */

      sprite.setPosition(body.getX, body.getY)
    }
  }


  //destroys collisions map and marks that this can be cleaned
  def destroy(): Unit = {
    collDetect.removeShape(body)
    removed = true
    visible = false
    enabled = false
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

