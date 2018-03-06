package game

import com.badlogic.gdx._
import game.loader.{GameTextures, Loader}
import game.main.MainGame


class HackOfWar extends Game {


  private var inputMultiplexer: InputMultiplexer = _

  private var screenDimensions: World = new World(1920, 1080, 2280, 1440)


  override def create() {

    //creates the game

    //sets the touch listener
    /*
    inputMultiplexer = new InputMultiplexer()
    inputMultiplexer.addProcessor(game.inputProcessors)
    Gdx.input.setInputProcessor(inputMultiplexer)
    */

    Gdx.app.setLogLevel(Application.LOG_DEBUG)

    val textures = new GameTextures()

    this.setScreen(
      new Loader(
        textures,
        () => this.setScreen(new MainGame(textures, screenDimensions))
      ))


  }

  override def render() {
    super.render()
  }

  override def resize(width: Int, height: Int): Unit = {
    super.resize(width, height)
  }
}