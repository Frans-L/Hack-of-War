package game.loader

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AssetLoader
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter

/**
  * Created by Frans on 01/03/2018.
  */
trait Loadable {

  val load: Vector[(String, Class[_])]
  val loadFont: Vector[(String, Class[BitmapFont], FreeTypeFontLoaderParameter)]

  def finished(manager: AssetManager): Unit

  //def dispose(): Unit = ??? //TODO: Dispose


}
