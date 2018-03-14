package game.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite, TextureRegion}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.{Polygon, Vector2}
import game.Ticker
import game.main.{CollisionBody, CollisionDetector, MainGame}
import game.util.{Utils, Vector2e}
import game.util.Vector2e._

/**
  * Created by Frans on 26/02/2018.
  */
class ActiveObject(var sprite: Sprite, collDetect: CollisionDetector, val collBody: CollisionBody,
                   val pos: Vector2, val size: Vector2) extends GameObject {

  override val origin: Vector2 = Vector2e(size.x / 2f, size.y / 2f)

  val velocity: Vector2 = Vector2e(0f, 0f)

  val maxVelocity: Float = 0.2f
  val maxForce: Float = 0.05f
  val mass: Float = 50f
  val maxSeeAhead = sWidth * 2
  val maxForceAvoid = 0.1f

  updateCollPolygon()
  collDetect.addShape(collBody)

  updateSprite()


  private def target: Vector2 = MainGame.debugViewPort.unproject(Vector2e.pool(Gdx.input.getX, Gdx.input.getY))


  override def update(): Unit = {
    if (enabled) {

      if (!collDetect.isCollided(collBody) && pos.x < 1920 / 2f) {

        //move towards target
        val steering =
          (((target -- pos).nor ** maxVelocity) -- velocity)
            .limit(maxForce) */ mass

        val avoid = Vector2e.pool
        //if no obstacle at close distance found, try look further away
        if (!avoidObstacles(0, avoid)) avoidObstacles(maxSeeAhead, avoid)
        steering ++ avoid

        velocity.mulAdd(steering, ticker.delta).limit(maxVelocity)
        pos.mulAdd(velocity, ticker.delta)

        angle = velocity.angle

        Vector2e.free(steering) //free the memory
        Vector2e.free(avoid)
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


  //Sets the 'avoid' Vector2 to push the player around the obstacle
  //Retuns true if there were an obstacle
  private def avoidObstacles(visionLength: Float, avoid: Vector2): Boolean = {

    val ahead = Vector2e.pool(velocity).nor **
      (visionLength * (velocity.len() / maxVelocity)) ++ pos

    collBody.setPosition(ahead.x - origin.x, ahead.y - origin.y)
    val obstacle = collDetect.collideAsCircle(collBody)

    if(obstacle.isDefined)
      MainGame.debugRender.circle(collBody.center.x, collBody.center.y, collBody.getRadiusScaled)
    collBody.setPosition(pos.x - origin.x, pos.y - origin.y)

    //calculate the force opposite to obstacle center
    obstacle.foreach(o => ((avoid ++ pos) -- o.center).nor ** maxForceAvoid */ mass)
    Vector2e.free(ahead)

    obstacle.isDefined
  }


  //Destroys collisions map and marks that this can be cleaned
  def destroy(): Unit = {
    collDetect.removeShape(collBody)
    deleted = true
  }

  //Updates collPolygons location, rotation and scale
  def updateCollPolygon(): Unit = {
    collBody.setPosition(pos.x - origin.x, pos.y - origin.y)
    collBody.setScale(scale.x, scale.y)
    collBody.setRotation(angle)
    collBody.setOrigin(origin.x, origin.y)
  }

  override def draw(shapeRender: ShapeRenderer): Unit = {
    if (visible) {
      //shapeRender.polygon(collPolygon.getTransformedVertices)
      //shapeRender.circle(pos.x, pos.y, 5)
    }
  }

  override def draw(batch: Batch): Unit = {
    if (visible) {
      sprite.draw(batch)
    }
  }


}

