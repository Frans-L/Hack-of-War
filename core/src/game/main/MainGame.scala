package game.main

import java.util.concurrent.TimeUnit

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.badlogic.gdx.graphics.{FPSLogger, GL20, OrthographicCamera}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.{ExtendViewport, Viewport}
import com.badlogic.gdx.{Gdx, Screen}
import game.loader.GameTextures
import game.ui.GameUI
import game.util.{Vector2e, Vector2mtv}
import game.{Ticker, World}


/**
  * For the debug reasons
  */
object MainGame {

  var debugViewPort: Viewport = _
  var debugRender: ShapeRenderer = _

  def setColorBlack(): Unit = MainGame.debugRender.setColor(0, 0, 0, 1)
  def setColorWhite(): Unit = MainGame.debugRender.setColor(1, 1, 1, 1)
  def setColorMagenta(): Unit = MainGame.debugRender.setColor(1, 0, 1, 1)
  def setColorRed(): Unit = MainGame.debugRender.setColor(1, 0, 0, 1)
}

/**
  * Created by Frans on 26/02/2018.
  */
class MainGame(textures: GameTextures, screenDim: World) extends Screen {


  //sets the ticker => everything should be time dependent
  private val ticker = new Ticker(TimeUtils.millis())
  Ticker.defaultTicker = ticker //to future gameElements
  ticker.speed = 1f

  //sets the drawing batches
  private val batch: SpriteBatch = new SpriteBatch
  private val shapeRender: ShapeRenderer = new ShapeRenderer

  MainGame.debugRender = new ShapeRenderer
  MainGame.debugRender.setColor(1, 0, 1f, 1)

  //sets the game camera
  private val cam: OrthographicCamera = new OrthographicCamera()
  private val viewport: Viewport = new ExtendViewport(
    screenDim.width, screenDim.height,
    screenDim.maxWidth, screenDim.maxHeight, cam)
  viewport.apply()

  MainGame.debugViewPort = viewport

  //sets the map
  private val map: Map = new Map(screenDim, textures)

  private val collDetect: CollisionHandler = new CollisionHandler(map)

  //sets the players
  private val players: Vector[Player] = Vector(
    new Player(textures, collDetect, 0)
  )

  //sets the ui
  private val gameUI: GameUI =
    new GameUI(textures, screenDim, viewport, players.head, shapeRender)

  val fPSLogger: FPSLogger = new FPSLogger


  /**
    * Called every frame
    * @param delta Libgdx's mandatory parameter
    */
    override def render(delta: Float): Unit = {

    //clears the screen
    Gdx.gl.glClearColor(74 / 255f, 96 / 255f, 112 / 255f, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    //updates game
    ticker.update(TimeUtils.millis())


    this.draw() //TODO update should be before draw, change when debug isn't needed

    MainGame.debugRender.setProjectionMatrix(cam.combined)
    MainGame.debugRender.begin(ShapeType.Line)
    this.update()
    MainGame.debugRender.end()



    if (ticker.interval10) {
      Gdx.app.log("MainGame", "Render calls: " + batch.renderCalls)
    }


    fPSLogger.log()

    //TimeUnit.MILLISECONDS.sleep(400)

  }

  override def show(): Unit = Unit

  override def resume(): Unit = Unit

  override def pause(): Unit = Unit

  override def hide(): Unit = Unit

  override def dispose(): Unit = Unit


  def draw(): Unit = {

    batch.setProjectionMatrix(cam.combined)

    batch.begin()
    players.foreach(_.draw(batch))
    map.draw(batch)
    batch.end()

    shapeRender.setProjectionMatrix(cam.combined)
    shapeRender.begin(ShapeType.Line)

    shapeRender.setColor(1, 0, 1, 1)

    /*
    shapeRender.circle(0, 0, 25)
    shapeRender.rect(screenDim.left, screenDim.maxDown, screenDim.width - 1, screenDim.maxHeight - 1)
    shapeRender.rect(screenDim.maxLeft, screenDim.down, screenDim.maxWidth - 1, screenDim.height - 1)
    a.draw(shapeRender)
     */

    shapeRender.setColor(0, 0.75f, 0.75f, 1)
    gameUI.draw(shapeRender)
    players.foreach(_.draw(shapeRender))

    shapeRender.end()

  }

  def update(): Unit = {

    cam.update()
    players.foreach(_.update())
    gameUI.update()

  }

  def resize(width: Int, height: Int): Unit = {
    viewport.update(width, height, true)
    cam.position.set(0, 0, 0)
    cam.update()

  }


  //return all inputProcessors aka input listeners
  //def inputProcessors = gameUI.inputProcessor


}
