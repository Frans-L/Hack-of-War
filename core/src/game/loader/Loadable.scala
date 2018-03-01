package game.loader

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AssetLoader

/**
  * Created by Frans on 01/03/2018.
  */
trait Loadable {

  val load: Vector[(String, Class[_])]

  def finished(manager: AssetManager): Unit

  //def dispose(): Unit = ??? //TODO: Dispose


}
