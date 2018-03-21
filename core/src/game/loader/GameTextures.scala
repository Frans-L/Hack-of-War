package game.loader

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AssetLoader
import com.badlogic.gdx.graphics.g2d.TextureAtlas

/**
  * Created by Frans on 01/03/2018.
  */
class GameTextures extends Loadable {

  val load: Vector[(String, Class[_])] = Vector((GameTextures.atlasName, classOf[TextureAtlas]))

  var atlas: TextureAtlas = _

  override def finished(manager: AssetManager): Unit = {
    atlas = manager.get(load(0)._1, classOf[TextureAtlas])
  }

}


object GameTextures {

  val atlasName = "graphics.atlas"

  object Units {
    val unit1 = "unit1"
    val bullet1 = "bullet"
    val card1 = "card1"
    val mapBorderShort = "mapBorderShort"
    val mapBorderWide = "mapBorderWide"
    val mapBorder = "mapBorder"
  }

}
