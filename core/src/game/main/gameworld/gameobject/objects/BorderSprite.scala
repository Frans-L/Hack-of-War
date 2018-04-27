package game.main.gameworld.gameobject.objects

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import game.main.gameworld.gameobject.{GameObject, ObjectHandler}
import game.main.gameworld.gameobject.ObjectHandler.Level
import game.main.gameworld.gameobject.elements.SpriteElement

object BorderSprite {

  def apply(pos: Vector2, size: Vector2, padding: Vector2,
            sprite: Sprite, objectHandler: ObjectHandler,
            level: Level.Value): GameObject = {

    val outer = new GameObject()
    outer.pos.set(pos)
    outer.size.set(size)
    val inner = new GameObject().appendElement(new SpriteElement(sprite, false))
    inner.pos.set(padding.x, padding.y)
    inner.size.set(size.x - 2 * padding.y, size.y - 2 * padding.y)

    outer.appendElement(inner)
    outer.update()

    objectHandler.addObject(outer, level, update = false)
    outer

  }

}
