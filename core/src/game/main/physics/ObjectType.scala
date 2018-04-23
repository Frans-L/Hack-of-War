package game.main.physics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector
import com.badlogic.gdx.math.Vector2
import game.GameElement
import game.loader.{GameTextures, UnitTextures}
import game.main.MainGame
import game.main.physics.collision.CollisionBody
import game.util.Vector2e._
import game.util.{Vector2e, Vector2mtv}

import scala.collection.mutable

/**
  * Created by Frans on 26/02/2018.
  */
trait ObjectType extends SpriteType {

  //all variables that are inherited
  /*
  override var visible: Boolean = true
  override var enabled: Boolean = true
  override var deleted: Boolean = false

  override var sprite: Sprite

  override val size: Vector2
  override val scale: Vector2 = Vector2e(1f, 1f)
  override val origin: Vector2 = Vector2e(0, 0)

  override val pos: Vector2
  override var angle: Float = 0f //degrees

  */

  //physics to gameObject
  val physWorld: PhysicsWorld
  var mass: Float
  var friction: Float
  val velocity: Vector2 = Vector2e(0f, 0f)

  var collToMe: Boolean = true //if others objects checks collision with this object
  var collToOthers: Boolean = true //if this object checks collision with other objects

  //this object checks collision only with these filtered objects, if Option is defined
  var collFilter: mutable.Buffer[GameElement] = mutable.Buffer.empty
  val collBody: CollisionBody //the collision body

  //shadows
  protected val shadow: Option[Sprite] = None
  val shadowPos: Vector2 = Vector2e(0, 0) //can be null since it is only used with shadow (option)

  /** After changing pos / size / origin etc. It's good to update
    * sprite and the collBody immediately. */
  def updateShape(): Unit = {
    updateSprite()
    updateCollPolygon()
  }


  /** Updates physics */
  def updatePhysics(): Unit = {

    var crash = false

    //checks collision
    if (collToOthers) {
      val collForce = Vector2mtv.pool()
      val crashObj = physWorld.collide(this, collForce, collFilter)
      crashObj.foreach(obj => crash = collision(obj, collForce))
      Vector2mtv.free(collForce) //free the memory
    }

    //if free to move
    if (!crash) {
      pos.mulAdd(velocity, ticker.delta)
    }

    //reduces the velocity if the total friction isn't zero
    val fric = friction + physWorld.globalFriction
    if (fric != 0) velocity.scl(1f / fric)

    updateCollPolygon(collBody) //update collisionBox coords
  }


  /** Will be called when collision happened by updatePhysics.
    * Thse default action is to move out of the object and "bounce" back.
    *
    * @return True if the collision was accepted (default) */
  protected def collision(crashObj: ObjectType, collForce: MinimumTranslationVector): Boolean = {
    pos.mulAdd(collForce.normal, collForce.depth + 0.1f) //move out of the collision
    velocity.setAngle(collForce.normal.angle()) //bounce back
    true
  }

  /** Updates collPolygons location, rotation and scale.
    * updatePhysics calls this automatically. */
  protected def updateCollPolygon(body: CollisionBody = collBody): Unit = {
    body.setPosition(pos.x - origin.x, pos.y - origin.y)
    body.setScale(scale.x, scale.y)
    body.setRotation(angle)
    body.setOrigin(origin.x, origin.y)
  }

  /** ActiveObject should be destroyable */
  def destroy(): Unit = {
    deleted = true
  }


  /** Adds a force to the object  */
  def addForce(force: Vector2): Unit = {
    velocity.mulAdd(force, 1f / mass) // F = MA => A = F/M
  }

  /** Adds an impact to the object that will add a force */
  def addImpact(vel: Vector2, mass2: Float): Unit = {
    val speed =
      Vector2e.pool(velocity).scl(-math.abs(math.cos(velocity.angleRad(vel))).toFloat) ++ vel
    velocity.mulAdd(speed, mass2 / mass)
    Vector2e.free(speed)
  }

  /** Updates the sprites */
  override def updateSprite(): Unit = {
    updateSprite(sprite)

    shadow.foreach(s => {
      updateSprite(s)
      s.translateX(shadowPos.x + physWorld.globalShadowPos.x)
      s.translateY(shadowPos.y + physWorld.globalShadowPos.y)
    })
  }


  /** Draws the collision boxes */
  override def draw(shapeRender: ShapeRenderer): Unit = {
    if (MainGame.drawCollBox) {
      collBody.draw(shapeRender)
    }
  }

  /** Draws the sprites */
  override def draw(batch: Batch): Unit = {
    if (visible) {
      shadow.foreach(s => s.draw(batch))
      sprite.draw(batch)
    }
  }

  /** Returns the shadow if it exists */
  protected def createShadow(unitTextures: UnitTextures): Option[Sprite] = {
    if (unitTextures.shadow.length > 0) {
      val s = GameTextures.defaultTextures.atlas.createSprite(unitTextures.shadow)
      s.setAlpha(0.7f)
      Some(s)
    } else None
  }

  /** Returns the main sprite if it exists */
  protected def createSprite(unitTextures: UnitTextures, colorIndex: Int): Option[Sprite] = {
    if (unitTextures.main(colorIndex).length > 0)
      Some(GameTextures.defaultTextures.atlas.createSprite(unitTextures.main(colorIndex)))
    else None
  }


}

