package game.main.gameworld.gameobject.elements

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite, TextureRegion}
import com.badlogic.gdx.math.Vector2
import game.loader.GameTextures
import game.main.gameworld.gameobject.{GameObject, ObjectElement, elements}
import game.util.Vector2e

/** Acts as a shadow to a parent Sprite. */
class ShadowElement(texture: TextureRegion, parent: Sprite) extends ObjectElement {

  private val shadowPos = Vector2e(-3, -4)

  override def update(parentObject: GameObject, delta: Int): Unit = {
    texture.flip(parent.isFlipX, parent.isFlipY)
  }

  override def draw(parentObject: GameObject, batch: Batch): Unit = {
    batch.draw(texture,
      parent.getX + shadowPos.x, parent.getY + shadowPos.y,
      parent.getOriginX, parent.getOriginY,
      parent.getWidth, parent.getHeight,
      parent.getScaleX, parent.getScaleY,
      parent.getRotation
    )
  }

  /** Throws an error if the parent is not valid! */
  override def checkParent(parent: GameObject): Unit = Unit //all fits
}
