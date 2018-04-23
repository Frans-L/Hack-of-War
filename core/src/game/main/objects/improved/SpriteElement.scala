package game.main.objects.improved

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}


class SpriteElement(var sprite: Sprite) extends ObjectElement {

  override def update(f: GameObject, delta: Int) {
    //updates sprite loc
    sprite.setOrigin(f.origin.x, f.origin.y)
    sprite.setBounds(f.pos.x - f.origin.x, f.pos.y - f.origin.y, f.size.x, f.size.y)
    sprite.setScale(f.scale.x, f.scale.y)
    sprite.setRotation(f.angle)

    super.update(f, delta)
  }

  override def draw(batch: Batch): Unit = {

    sprite.draw(batch)
    super.draw(batch)
  }
}
