package game

import com.badlogic.gdx.{Gdx, InputMultiplexer, InputProcessor}
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.utils.viewport.{ExtendViewport, Viewport}
import game.objects.ActiveObject

/**
  * Created by Frans on 26/02/2018.
  */
class Game(ticker: Ticker, world: World) {

  //sets the drawing batches
  private val batch: SpriteBatch = new SpriteBatch
  private val shapeRender: ShapeRenderer = new ShapeRenderer

  //sets the game camera
  private val cam: OrthographicCamera = new OrthographicCamera()
  private val viewPort: Viewport = new ExtendViewport(
    world.width, world.height, world.maxWidth, world.maxHeight, cam)
  viewPort.apply()

  //sets the ui
  private val gameUI: ui.GameUI = new ui.GameUI(ticker, world)


  val a: ActiveObject = new ActiveObject(ticker, 0.0, 0.0)

  def draw(): Unit = {

    batch.setProjectionMatrix(cam.combined)
    batch.begin()
    batch.end()

    shapeRender.setProjectionMatrix(cam.combined)
    shapeRender.begin(ShapeType.Line)

    shapeRender.setColor(1, 0, 1, 1)
    shapeRender.circle(0, 0, 25)
    shapeRender.rect(world.left, world.maxDown, world.width - 1, world.maxHeight - 1)
    shapeRender.rect(world.maxLeft, world.down, world.maxWidth - 1, world.height - 1)
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

    gameUI.resize(width, height)

    viewPort.update(width, height, true)
    cam.position.set(0, 0, 0)
    cam.update()
  }


  //return all inputProcessors aka input listeners
  def inputProcessors: Vector[InputProcessor] = Vector[InputProcessor](gameUI.inputListener)

}
