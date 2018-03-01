package game

import com.badlogic.gdx.{Gdx, InputMultiplexer, InputProcessor, Screen}
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera}
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.{ExtendViewport, Viewport}
import game.objects.ActiveObject
import game.ui.GameUI

/**
  * Created by Frans on 26/02/2018.
  */
class MainGame(screenDim: World) extends Screen {

  private val ticker = new Ticker(TimeUtils.millis()) //everything should be time dependent

  //sets the drawing batches
  private val batch: SpriteBatch = new SpriteBatch
  private val shapeRender: ShapeRenderer = new ShapeRenderer

  //sets the game camera
  private val cam: OrthographicCamera = new OrthographicCamera()
  private val viewport: Viewport = new ExtendViewport(
    screenDim.width, screenDim.height,
    screenDim.maxWidth, screenDim.maxHeight, cam)
  viewport.apply()

  //sets the ui
  private val gameUI: GameUI = new GameUI(ticker, screenDim, viewport, shapeRender)

  val a: ActiveObject = new ActiveObject(ticker, 0.0, 0.0)


  //called every frame
  override def render(delta: Float): Unit = {

    //clears the screen
    Gdx.gl.glClearColor(1, 1, 1, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    //updates game
    ticker.update(TimeUtils.millis())
    this.update()
    this.draw()

  }

  override def show(): Unit = Unit

  override def resume(): Unit = Unit

  override def pause(): Unit = Unit

  override def hide(): Unit = Unit

  override def dispose(): Unit = Unit


  def draw(): Unit = {

    batch.setProjectionMatrix(cam.combined)
    batch.begin()
    batch.end()

    shapeRender.setProjectionMatrix(cam.combined)
    shapeRender.begin(ShapeType.Line)

    shapeRender.setColor(1, 0, 1, 1)
    shapeRender.circle(0, 0, 25)
    shapeRender.rect(screenDim.left, screenDim.maxDown, screenDim.width - 1, screenDim.maxHeight - 1)
    shapeRender.rect(screenDim.maxLeft, screenDim.down, screenDim.maxWidth - 1, screenDim.height - 1)
    a.draw(shapeRender)

    shapeRender.setColor(0, 0.75f, 0.75f, 1)
    gameUI.draw(shapeRender)

    shapeRender.end()

  }

  def update(): Unit = {

    cam.update()
    a.update()

  }

  def resize(width: Int, height: Int): Unit = {
    viewport.update(width, height, true)
    cam.position.set(0, 0, 0)
    cam.update()

  }


  //return all inputProcessors aka input listeners
  //def inputProcessors = gameUI.inputProcessor


}
