package game.main.physics.objects.units

import com.badlogic.gdx.math.Vector2
import game.GameElement
import game.loader.GameTextures
import game.main.physics.PhysicsWorld
import game.main.physics.collision.{CircleBody, CollisionBody}
import game.main.physics.objects.BulletObject
import game.util.{Utils, Vector2e}

object BasicBullet {

  val texture: Seq[String] = GameTextures.Units.bullet1

  //size info
  private val scale = 1.0f
  val radius = 20f / 2f / scale

  def create(owner: GameElement, physWorld: PhysicsWorld,
             pos: Vector2, velocity: Vector2,
             colorIndex: Int): BulletObject = {

    val body: CollisionBody = new CircleBody(radius)
    //Utils.rectangleCollBody(0, 0, w.toInt, h.toInt)

    val bullet: BulletObject = new BulletObject(
      GameTextures.defaultUITextures.atlas.createSprite(texture(colorIndex)),
      owner, physWorld, body,
      pos, velocity.scl(1f), Vector2e(radius * 2f, radius * 2f)
    )

    bullet.collOn = false //make sure that bullet

    bullet
  }
}
