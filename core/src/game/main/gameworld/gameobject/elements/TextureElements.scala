package game.main.gameworld.gameobject.elements

import com.badlogic.gdx.graphics.g2d.{Batch, Sprite, TextureRegion}
import com.badlogic.gdx.math.Vector2
import game.GameElement
import game.main.gameworld.gameobject
import game.main.gameworld.gameobject.{GameObject, ObjectElement}
import game.util.Vector2e

/** Draws the texture over the object. */
class TextureElement(var texture: TextureRegion) extends ObjectElement {

  override def draw(parent: GameObject, batch: Batch): Unit = {
    batch.draw(texture,
      parent.pos.x - parent.origin.x, parent.pos.y - parent.origin.y,
      parent.origin.x, parent.origin.y,
      parent.size.x, parent.size.y,
      parent.scale.x, parent.scale.y,
      parent.angle
    )
  }

  /** Throws an error if the parent is not valid! */
  override def checkParent(parent: GameObject): Unit = Unit //all fits
}

/** Draws the texture over the object relatively. */
class RelativeTextureElement(texture: TextureRegion,
                             val pos: Vector2 = Vector2e(0, 0),
                             val scale: Vector2 = Vector2e(1, 1),
                             var angle: Float = 0,
                             var overrideSize: Option[Vector2] = None) extends TextureElement(texture) {

  override def draw(parent: GameObject, batch: Batch): Unit = {
    batch.draw(texture,
      parent.pos.x - parent.origin.x + pos.x, parent.pos.y - parent.origin.y + pos.y,
      parent.origin.x, parent.origin.y,
      overrideSize.getOrElse(parent.size).x, overrideSize.getOrElse(parent.size).y,
      parent.scale.x * scale.x, parent.scale.y * scale.y,
      parent.angle + angle
    )
  }

}

/** Draws the shadow to object. */
class ShadowElement(texture: TextureRegion)
  extends RelativeTextureElement(texture) {
  pos.set(-3, -4)
}

/** Draws a static texture over the object. */
class StaticTextureElement(texture: TextureRegion) extends RelativeTextureElement(texture) {

  var sprite: Sprite = new Sprite(texture) //sprites calculates the vertices only when updated
  private var dirty = true

  def update(): Unit = dirty = true

  override def update(parent: GameObject, delta: Int) {
    if (dirty) {
      sprite.setOrigin(parent.origin.x, parent.origin.y)
      sprite.setPosition(parent.pos.x - parent.origin.x, parent.pos.y - parent.origin.y)
      sprite.setSize(overrideSize.getOrElse(parent.size).x, overrideSize.getOrElse(parent.size).y)
      sprite.setScale(parent.scale.x, parent.scale.y)
      sprite.setRotation(parent.angle)
      dirty = false
    }
  }

  override def draw(parent: GameObject, batch: Batch): Unit = {
    sprite.draw(batch)
  }

}