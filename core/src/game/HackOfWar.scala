package game

import com.badlogic.gdx.{Application, ApplicationAdapter, Gdx}
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera, Texture}
import com.badlogic.gdx.graphics.g2d.{Sprite, SpriteBatch}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport._

class HackOfWar extends ApplicationAdapter {

  private var batch: SpriteBatch = _
  private var shapeRender: ShapeRenderer = _
  private var cam: OrthographicCamera = _
  private var viewPort: Viewport = _
  private var world: World = _

  private var game: Game = _
  private var ticker: Ticker = _

  override def create() {

    //creates draw batches
    batch = new SpriteBatch
    shapeRender = new ShapeRenderer

    //sets world and camera
    world = new World(1920, 1080, 2280, 1440)
    cam = new OrthographicCamera()
    viewPort = new ExtendViewport(world.width, world.height, world.maxWidth, world.maxHeight, cam)
    viewPort.apply()

    //creates the game
    ticker = new Ticker(TimeUtils.millis())
    game = new Game(world)

  }

  override def render() {


    //updates camera
    cam.update()
    batch.setProjectionMatrix(cam.combined)
    shapeRender.setProjectionMatrix(cam.combined)

    //clear screen
    Gdx.gl.glClearColor(1, 1, 1, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    //update game
    ticker.update(TimeUtils.millis())
    game.update(ticker)

    //draw
    batch.begin()
    batch.end()
    shapeRender.begin(ShapeType.Line)

    game.draw(shapeRender)

    shapeRender.end()

    Gdx.app.setLogLevel(Application.LOG_DEBUG)


  }

  override def resize(width: Int, height: Int): Unit = {
    viewPort.update(width, height, true)
    cam.position.set(0, 0, 0)
    cam.update()
  }
}