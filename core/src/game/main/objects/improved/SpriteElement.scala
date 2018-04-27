package game.main.objects.improved

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}


class SpriteElement(var sprite: Sprite, keepSize: Boolean) extends ObjectElement {

  override def update(parent: GameObject, delta: Int) {
    //updates sprite loc
    sprite.setOrigin(parent.origin.x, parent.origin.y)
    if (keepSize) sprite.setPosition(parent.pos.x - parent.origin.x, parent.pos.y - parent.origin.y)
    else sprite.setBounds(parent.pos.x - parent.origin.x,
      parent.pos.y - parent.origin.y, parent.size.x, parent.size.y)
    sprite.setScale(parent.scale.x, parent.scale.y)
    sprite.setRotation(parent.angle)
  }

  override def draw(parent: GameObject, batch: Batch): Unit = {
    sprite.draw(batch)
  }

  /** Throws an error if the parent is not valid! */
  override def checkParent(parent: GameObject): Unit = Unit //all fits
}
