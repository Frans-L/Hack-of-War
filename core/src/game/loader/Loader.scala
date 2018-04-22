package game.loader

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.g2d.{BitmapFont, TextureAtlas}
import com.badlogic.gdx.graphics.g2d.freetype.{FreeTypeFontGenerator, FreeTypeFontGeneratorLoader, FreetypeFontLoader}
import com.badlogic.gdx.{Gdx, Screen}

/**
  * Created by Frans on 28/02/2018.
  */
class Loader(target: Loadable, nextScreen: () => Unit) extends Screen {

  val manager: AssetManager = new AssetManager

  val resolver: FileHandleResolver = new InternalFileHandleResolver
  manager.setLoader(classOf[FreeTypeFontGenerator], new FreeTypeFontGeneratorLoader(resolver))
  manager.setLoader(classOf[BitmapFont], ".ttf", new FreetypeFontLoader(resolver))

  target.load.foreach(i => manager.load(i._1, i._2)) //load all elements
  target.loadFont.foreach(i => manager.load(i._1, i._2, i._3)) //load all fonts

  var totaltime: Float = 0

  override def render(delta: Float): Unit = {

    totaltime += delta

    if (manager.update()) {
      target.finished(manager)
      nextScreen()
    }
    else
      Gdx.app.log("Loading...", "" + (totaltime * 1000).toInt + " ms")
  }

  override def resume(): Unit = Unit

  override def show(): Unit = Unit

  override def pause(): Unit = Unit

  override def hide(): Unit = Unit

  override def resize(width: Int, height: Int): Unit = Unit

  override def dispose(): Unit = Unit


}
