package game.main.physics.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import game.GameElement
import game.main.{MainGame, Player}
import game.main.physics.PhysicsWorld
import game.main.physics.collision.{CircleBody, CollisionBody, PolygonBody}
import game.util.{Vector2e, Vector2mtv}

class BulletObject(override var sprite: Sprite, var owner: GameElement,
                   override val physWorld: PhysicsWorld, override val collBody: CollisionBody,
                   override val pos: Vector2, override val velocity: Vector2,
                   override val size: Vector2) extends ObjectType {

  override var mass: Float = 0.25f
  override var friction: Float = 0

  //bullet stats
  var damage: Float = 50f //will be overridden
  var lifeTime: Int = 1000

  override val origin: Vector2 = Vector2e(size.x / 2f, size.y / 2f)

  updateCollPolygon()
  updateSprite()
  physWorld.addUnit(owner, this) //adds update calls


  override def update(): Unit = {

    val collForce = Vector2mtv.pool()
    val crashObj = physWorld.collide(this, collForce)

    if (crashObj.isEmpty) {
      moveForward()
    } else {

      crashObj.get match {

        case obj: UnitObject =>
          obj.reduceHealth(damage)
          obj.addForce(velocity.scl(mass * 100))
          this.destroy()

        case wall: CollisionObject =>
          this.destroy()

        case _ => moveForward()
      }
    }

    lifeTime -= ticker.delta

    if (lifeTime <= 0) this.destroy()

    updateSprite()
    updateCollPolygon()
  }

  private def moveForward(): Unit = {
    pos.mulAdd(velocity, ticker.delta)

  }

  override def draw(shapeRender: ShapeRenderer): Unit = {

  }

  override def draw(batch: Batch): Unit = {
    if (visible) {
      sprite.draw(batch)
    }
  }

}
