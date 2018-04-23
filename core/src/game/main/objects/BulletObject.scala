package game.main.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.{Intersector, Vector2}
import game.GameElement
import game.loader.UnitTextures
import game.main.MainGame
import game.main.physics.{ObjectType, PhysicsWorld}
import game.main.physics.collision.{CircleBody, CollisionBody, PolygonBody}
import game.main.players.Player
import game.util.Vector2e

class BulletObject(textures: UnitTextures, colorIndex: Int, override val size: Vector2,
                   var owner: GameElement, override var physWorld: PhysicsWorld,
                   override var collBody: CollisionBody,
                   override val pos: Vector2, override val velocity: Vector2) extends ObjectType {

  //sprites
  override var sprite: Sprite = this.createSprite(textures, colorIndex).get
  shadow = this.createShadow(textures)

  //physic object
  mass = 50f
  friction = 0

  //bullet stats
  var damage: Float = 0f //will be overridden
  var lifeTime: Int = 1000

  override val origin: Vector2 = Vector2e(size.x / 2f, size.y / 2f)

  updateCollPolygon(collBody)
  updateSprite()
  physWorld.addUnit(owner, this) //adds to update calls


  override def update(): Unit = {

    lifeTime -= ticker.delta
    if (lifeTime <= 0) this.destroy()

    updatePhysics()
    updateSprite()
  }


  /** Replaces basic collision action with bullet's own. */
  override protected def collision(crashObj: ObjectType,
                                   collForce: Intersector.MinimumTranslationVector): Boolean = {

    crashObj match {

      case obj: UnitObject =>
        obj.reduceHealth(damage)
        obj.addImpact(velocity.scl(1f), mass)
        this.destroy()
        true

      case wall: CollisionObject =>
        this.destroy()
        true

      case _ => false //no collision detected
    }

  }

  private def moveForward(): Unit = {
    pos.mulAdd(velocity, ticker.delta)

  }

  override def draw(shapeRender: ShapeRenderer): Unit = {
    if (MainGame.drawCollBox) {
      collBody.draw(shapeRender)
    }
  }

  override def draw(batch: Batch): Unit = {
    if (visible) {
      sprite.draw(batch)
    }
  }

  override def reset(): Unit = ???

}
