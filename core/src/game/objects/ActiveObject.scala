package game.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite, TextureRegion}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.{Polygon, Vector2}
import game.Ticker
import game.main.CollisionDetector
import game.util.{Utils, Vector2e}
import game.util.Vector2e

/**
  * Created by Frans on 26/02/2018.
  */
class ActiveObject(var sprite: Sprite, collDetect: CollisionDetector,
                   val pos: Vector2, val size: Vector2) extends GameObject {

  val velocity: Vector2 = Vector2e(0f, 0f)
  var maxVelocity: Float = 0
  var mass: Float = 0

  var collPolygon: Polygon = Utils.rectanglePolygon(pos.x, pos.y, size.x, size.y)
  updateCollPolygon()
  collDetect.addShape(collPolygon)

  updateSprite()


  private def targetX: Float = Gdx.input.getX

  private def targetY: Float = Gdx.input.getY

  override def update(): Unit = {
    if (enabled) {


      if (!collDetect.collide(collPolygon) && pos.x < 1920 / 2f) {

        //val steering = velocity - (Vector2(targetX - pos.x, targetY - pos.y).nor * maxVelocity)

        pos.x += 0.1f * ticker.delta

      }
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
    collPolygon.setPosition(pos.x, pos.y)
    collPolygon.setScale(scale.x, scale.y)
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

