package game.main.gameworld.gameobject.objects

import com.badlogic.gdx.graphics.g2d.{Sprite, TextureRegion}
import com.badlogic.gdx.math.Vector2
import game.main.gameworld.gameobject.{GameObject, ObjectHandler}
import game.main.gameworld.gameobject.ObjectHandler.Level
import game.main.gameworld.gameobject.elements.StaticTextureElement
import game.util.Vector2e

object BorderSprite {

  def apply(pos: Vector2, size: Vector2, padding: Vector2,
            textureRegion: TextureRegion): GameObject = {

    val outer = new GameObject()
    outer.pos.set(pos)
    outer.size.set(size)

    val texture = new StaticTextureElement(textureRegion)
    texture.pos.set(padding.x, padding.y)
    texture.overrideSize = Some(Vector2e(size.x - 2 * padding.y, size.y - 2 * padding.y))
    texture.update()

    outer.appendElement(texture)
    outer.update()

    //objectHandler.addObject(outer, level, update = false)
    outer
  }

}
