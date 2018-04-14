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

class BulletObject(var sprite: Sprite, var owner: GameElement,
                   val physWorld: PhysicsWorld, val collBody: CollisionBody,
                   val pos: Vector2, val velocity: Vector2,
                   val size: Vector2) extends ObjectType {

  /*
  override val collBody: CollisionBody = _
  override val velocity: Vector2 = _
  override val mass: Float = _
  override val pos: Vector2 = _
  override val size: Vector2 = _
  override var sprite: Sprite = _
  */


  var lifeTime: Int = 1000
  val mass: Float = 10f

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
          obj.destroy()
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
