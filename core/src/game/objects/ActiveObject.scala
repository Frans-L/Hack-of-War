package game.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite, TextureRegion}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.{Polygon, Vector2}
import game.Ticker
import game.main.{CollisionDetector, MainGame}
import game.util.{Utils, Vector2e}
import game.util.Vector2e._

/**
  * Created by Frans on 26/02/2018.
  */
class ActiveObject(var sprite: Sprite, collDetect: CollisionDetector, val collPolygon: Polygon,
                   val pos: Vector2, val size: Vector2) extends GameObject {

  override val origin: Vector2 = Vector2e(size.x / 2f, size.y / 2f)

  val velocity: Vector2 = Vector2e(0f, 0f)
  var maxVelocity: Float = 0.2f
  var maxForce: Float = 0.05f
  var mass: Float = 50f

  updateCollPolygon()
  collDetect.addShape(collPolygon)

  updateSprite()


  private def target: Vector2 = MainGame.debugViewPort.unproject(Vector2e(Gdx.input.getX, Gdx.input.getY))


  override def update(): Unit = {
    if (enabled) {


      if (!collDetect.collide(collPolygon) && pos.x < 1920 / 2f) {

        val steering =
          (((target -- pos).nor ** maxVelocity) -- velocity)
            .limit(maxForce) */ mass

        velocity.mulAdd(steering, ticker.delta).limit(maxVelocity)
        pos.mulAdd(velocity, ticker.delta)

        angle = velocity.angle

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


  //Destroys collisions map and marks that this can be cleaned
  def destroy(): Unit = {
    collDetect.removeShape(collPolygon)
    deleted = true
  }

  //Updates collPolygons location, rotation and scale
  def updateCollPolygon(): Unit = {
    collPolygon.setPosition(pos.x - origin.x, pos.y - origin.y)
    collPolygon.setScale(scale.x, scale.y)
    collPolygon.setRotation(angle)
    collPolygon.setOrigin(origin.x, origin.y)
  }

  override def draw(shapeRender: ShapeRenderer): Unit = {
    if (visible) {
      //shapeRender.polygon(collPolygon.getTransformedVertices)
      shapeRender.circle(pos.x, pos.y, 5)
    }
  }

  override def draw(batch: Batch): Unit = {
    if (visible) {
      sprite.draw(batch)
    }
  }


}

