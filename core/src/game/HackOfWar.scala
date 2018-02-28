package game

import com.badlogic.gdx.{Application, ApplicationAdapter, Gdx, InputMultiplexer}
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera, Texture}
import com.badlogic.gdx.utils.TimeUtils


class HackOfWar extends ApplicationAdapter {


  private var world: World = _
  private var game: Game = _
  private var ticker: Ticker = _

  private var inputMultiplexer: InputMultiplexer = _

  override def create() {

    //creates the game
    ticker = new Ticker(TimeUtils.millis())
    world = new World(1920, 1080, 2280, 1440)
    game = new Game(ticker, world)

    //sets the touch listener
    /*
    inputMultiplexer = new InputMultiplexer()
    inputMultiplexer.addProcessor(game.inputProcessors)
    Gdx.input.setInputProcessor(inputMultiplexer)
    */

    Gdx.app.setLogLevel(Application.LOG_DEBUG)

  }

  override def render() {

    //clear screen
    Gdx.gl.glClearColor(1, 1, 1, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    //update game
    ticker.update(TimeUtils.millis())
    game.update()
    game.draw()


  }

  override def resize(width: Int, height: Int): Unit = {
    game.resize(width, height)
  }
}