package game

import com.badlogic.gdx.{Gdx, Screen}
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader
import com.badlogic.gdx.graphics.g2d.TextureAtlas

/**
  * Created by Frans on 28/02/2018.
  */
class StartLoading(nextScreen: () => Unit) extends Screen {

  val manager: AssetManager = new AssetManager()

  manager.load("graphics.atlas", classOf[TextureAtlas])

  override def render(delta: Float): Unit = {

    if (manager.update()){



      nextScreen()
    }
    else
      Gdx.app.log("Loading...", "" + manager.getProgress)
  }

  override def resume(): Unit = Unit

  override def show(): Unit = Unit

  override def pause(): Unit = Unit

  override def hide(): Unit = Unit

  override def resize(width: Int, height: Int): Unit = Unit

  override def dispose(): Unit = Unit


}
