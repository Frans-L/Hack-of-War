package game.loader

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.{Gdx, Screen}

/**
  * Created by Frans on 28/02/2018.
  */
class Loader(target: Loadable, nextScreen: () => Unit) extends Screen {

  val manager: AssetManager = new AssetManager()

  target.load.foreach(i => manager.load(i._1, i._2)) //load all elements

  override def render(delta: Float): Unit = {

    if (manager.update()) {
      target.finished(manager)
      nextScreen()
    }
    else
      Gdx.app.log("Loading...", "" + (delta * 1000).toInt + " ms")
  }

  override def resume(): Unit = Unit

  override def show(): Unit = Unit

  override def pause(): Unit = Unit

  override def hide(): Unit = Unit

  override def resize(width: Int, height: Int): Unit = Unit

  override def dispose(): Unit = Unit


}
