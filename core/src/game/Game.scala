package game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.utils.viewport.{ExtendViewport, Viewport}

/**
  * Created by Frans on 26/02/2018.
  */
class Game(ticker: Ticker, world: World) {

  private var batch: SpriteBatch = _
  private var shapeRender: ShapeRenderer = _
  private var cam: OrthographicCamera = _
  private var viewPort: Viewport = _

  val a: ActiveObject = new ActiveObject(ticker, 0.0, 0.0)

  create() //initializes the game

  def draw(): Unit = {

    batch.begin()
    batch.end()

    shapeRender.begin(ShapeType.Line)

    shapeRender.setColor(1, 0, 1, 1)
    shapeRender.circle(0, 0, 25)
    shapeRender.rect(world.left, world.maxDown, world.width - 1, world.maxHeight - 1)
    shapeRender.rect(world.maxLeft, world.down, world.maxWidth - 1, world.height - 1)
    a.draw(shapeRender)

    shapeRender.end()

  }

  def update(): Unit = {

    cam.update()
    batch.setProjectionMatrix(cam.combined)
    shapeRender.setProjectionMatrix(cam.combined)


    a.update()
  }


  def create(): Unit = {
    //creates draw batches
    batch = new SpriteBatch
    shapeRender = new ShapeRenderer

    //sets camera and viewport
    cam = new OrthographicCamera()
    viewPort = new ExtendViewport(world.width, world.height, world.maxHeight, world.maxHeight, cam)
    viewPort.apply()
  }


  def resize(width: Int, height: Int): Unit = {
    viewPort.update(width, height, true)
    cam.position.set(0, 0, 0)
    cam.update()
  }

}
