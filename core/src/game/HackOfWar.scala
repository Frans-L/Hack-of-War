package game

import com.badlogic.gdx._
import game.loader.{GameTextures, Loader}
import game.main.MainGame
import game.util.Dimensions


class HackOfWar extends Game {


  //is needed if multiple inputs listeners with different actors are used
  //private var inputMultiplexer: InputMultiplexer = _

  private var screenDimensions: Dimensions = new Dimensions(1920, 1080, 2280, 1440)

  override def create() {


    /*
    //sets the touch listener
    inputMultiplexer = new InputMultiplexer()
    inputMultiplexer.addProcessor(game.inputProcessors)
    Gdx.input.setInputProcessor(inputMultiplexer)
    */

    //creates the game
    Gdx.app.setLogLevel(Application.LOG_DEBUG)
    val textures = new GameTextures()
    this.setScreen(
      new Loader(
        textures,
        () => newGame()
      ))

    def newGame(): Unit = {
      this.setScreen(new MainGame(textures, screenDimensions, () => newGame()))
    }

  }



  override def render() {
    super.render()
  }

  override def resize(width: Int, height: Int): Unit = {
    super.resize(width, height)
  }
}