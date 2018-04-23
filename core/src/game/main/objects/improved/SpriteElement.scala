package game.main.objects.improved

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}


class SpriteElement(var sprite: Sprite) extends ObjectElement {

  override def update(delta: Int) {
    //updates sprite loc
    sprite.setOrigin(parent.origin.x, parent.origin.y)
    sprite.setBounds(parent.pos.x - parent.origin.x,
      parent.pos.y - parent.origin.y, parent.size.x, parent.size.y)
    sprite.setScale(parent.scale.x, parent.scale.y)
    sprite.setRotation(parent.angle)

    super.update(delta)
  }

  override def draw(batch: Batch): Unit = {

    sprite.draw(batch)
    super.draw(batch)
  }
}
