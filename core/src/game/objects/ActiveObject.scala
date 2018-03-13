package game.objects

import com.badlogic.gdx.graphics.g2d.{Batch, Sprite, TextureRegion}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Polygon
import game.{Ticker, Utils}
import game.main.CollisionDetector

/**
  * Created by Frans on 26/02/2018.
  */
class ActiveObject(var sprite: Sprite, collDetect: CollisionDetector,
                   var posX: Float, var posY: Float,
                   var width: Float, var height: Float) extends GameObject {


  var collPolygon: Polygon = Utils.rectanglePolygon(posX, posY, width, height)
  updateCollPolygon()
  collDetect.addShape(collPolygon)

  override def update(): Unit = {
    if (enabled) {


      if (!collDetect.collide(collPolygon) && posX < 1920 / 2f)
        posX += 0.1f * ticker.delta
      else
        destroy()


      /*
      Gdx.app.setLogLevel(Application.LOG_DEBUG)
      Gdx.app.log("tmp", "posX: " + posX)
      Gdx.app.log("tmp", "Ticker: " + ticker.elapsed)
      */

      updateSprite()
      updateCollPolygon()
    }
  }


  //destroys collisions map and marks that this can be cleaned
  def destroy(): Unit = {
    collDetect.removeShape(collPolygon)
    deleted = true
  }

  //Updates collPolygons location, rotation and scale
  def updateCollPolygon(): Unit = {
    collPolygon.setPosition(posX, posY)
    collPolygon.setScale(scaleX, scaleY)
    collPolygon.setRotation(angle)
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

