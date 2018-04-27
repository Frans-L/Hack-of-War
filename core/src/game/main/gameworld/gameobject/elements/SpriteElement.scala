package game.main.gameworld.gameobject.elements

import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import game.main.gameworld.gameobject
import game.main.gameworld.gameobject.ObjectElement


class SpriteElement(var sprite: Sprite, keepSize: Boolean) extends ObjectElement {

  override def update(parent: gameobject.GameObject, delta: Int) {
    //updates sprite loc
    sprite.setOrigin(parent.origin.x, parent.origin.y)
    if (keepSize) sprite.setPosition(parent.pos.x - parent.origin.x, parent.pos.y - parent.origin.y)
    else sprite.setBounds(parent.pos.x - parent.origin.x,
      parent.pos.y - parent.origin.y, parent.size.x, parent.size.y)
    sprite.setScale(parent.scale.x, parent.scale.y)
    sprite.setRotation(parent.angle)
  }

  override def draw(parent: gameobject.GameObject, batch: Batch): Unit = {
    sprite.draw(batch)
  }

  /** Throws an error if the parent is not valid! */
  override def checkParent(parent: gameobject.GameObject): Unit = Unit //all fits
}
