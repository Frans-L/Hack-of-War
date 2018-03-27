package game.main.physics.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.{Interpolation, Vector2}
import game.GameElement
import game.main.MainGame
import game.main.physics.{CollisionBody, PhysicsWorld}
import game.util.Vector2e._
import game.util.{Utils, Vector2e, Vector2mtv}

/**
  * Created by Frans on 26/02/2018.
  */
class UnitObject(var owner: GameElement, var sprite: Sprite,
                 val collHandler: PhysicsWorld, val collBody: CollisionBody,
                 val pos: Vector2, override val size: Vector2) extends ObjectType {


  val velocity: Vector2 = Vector2e(0f, 0f)
  override val origin: Vector2 = Vector2e(size.x / 2f, size.y / 2f)

  val maxVelocity: Float = 0.2f
  val maxForce: Float = 0.05f
  val mass: Float = 100f
  val maxSeeAhead: Float = sWidth * 2f
  val maxForceAvoid: Float = 0.25f
  val maxRotateTime: Float = 150f

  updateCollPolygon()

  updateSprite()


  private def target: Vector2 = MainGame.debugViewPort.unproject(Vector2e.pool(Gdx.input.getX, Gdx.input.getY))


  /**
    * Updates the object collision, physics, AI etc...
    */
  override def update(): Unit = {
    if (enabled) {

      val collForce = Vector2mtv.pool()
      if (collHandler.isCollided(this, collForce)) {

        pos.mulAdd((collForce.normal ** collForce.depth).limit(maxVelocity), ticker.delta)

      } else if (pos.x < 1920 / 2f - 150) { //TODO: 'if' because of the debugging

        //move towards target
        val steering =
          (((target -- pos).nor ** maxVelocity) -- velocity)
            .limit(maxForce) / mass

        //if no obstacle at close distance found, try look further away
        val avoid = Vector2e.pool()
        val obstacleFound = avoidObstacles(0, avoid)

        //if enough speed, try to look further away
        if (!obstacleFound && velocity.len2() > 0.001f) {
          avoidObstacles(maxSeeAhead, avoid)
        }

        //updates steering to avoid obstacles
        steering ++ avoid

        //moves object
        velocity.mulAdd(steering, ticker.delta).limit(maxVelocity)
        pos.mulAdd(velocity, ticker.delta)

        //rotates smoothly towards moving direction
        angle = Utils.closestAngle360(angle, velocity.angle)
        angle = Interpolation.linear.apply(angle, velocity.angle, ticker.delta / maxRotateTime)
        angle = Utils.absAngle(angle)

        //free the memory
        Vector2e.free(steering)
        Vector2e.free(avoid)

      }
      else
        destroy()

      Vector2mtv.free(collForce)

      updateSprite()
      updateCollPolygon()
    }
  }


  /** Check if obstacles are ahead. Sets the 'avoid' Vector2 to
    * push the player around the obstacle
    *
    * @param visionLength distance to look ahead
    * @param avoid        a Vector2 that is set to push the player away from the obstacle
    * @return true if there were an obstacle
    */
  private def avoidObstacles(visionLength: Float, avoid: Vector2): Boolean = {

    val ahead = Vector2e.pool(velocity).nor **
      (visionLength * (velocity.len() / maxVelocity)) ++ pos

    //checks collision in the wanted pos
    val visionPos = Vector2e.pool(ahead.x - origin.x, ahead.y - origin.y)
    val (obstacle, angle) =
      collHandler.collideAsCircle(this, ahead, collBody.getRadiusScaled)
    Vector2e.free(visionPos) //free the memory

    //draws debug circle
    //MainGame.debugRender.circle(ahead.x, ahead.y, collBody.getRadiusScaled)

    //calculate the force opposite to obstacle center
    obstacle.foreach(o => ((avoid ++ pos) -- o.collBody.center).nor ** maxForceAvoid / mass)
    Vector2e.free(ahead) //free the memory

    obstacle.isDefined //returns true if collided
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

