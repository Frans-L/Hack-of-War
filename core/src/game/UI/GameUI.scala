package game.UI

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.{ExtendViewport, Viewport}
import game._

import scala.collection.mutable

/**
  * Created by Frans on 27/02/2018.
  */
class GameUI(ticker: Ticker, world: World) extends GameElement {

  //sets UI camera
  private val cam: OrthographicCamera = new OrthographicCamera()
  private val viewPort: Viewport = new ExtendViewport(
    world.width, world.height, world.maxWidth, world.maxHeight, cam)
  viewPort.apply()

  //UI elements
  private val elements: mutable.ArrayBuffer[GameUIElement] = new mutable.ArrayBuffer[GameUIElement]()


  create()


  override def update(): Unit = ???


  override def draw(shapeRender: ShapeRenderer): Unit = {

    shapeRender.setProjectionMatrix(cam.combined)
    elements.foreach(_.draw(shapeRender))

  }

  def create(): Unit = {
    elements += new UICard(0, 0, 100, 100)
  }

  def resize(width: Int, height: Int): Unit = {
    viewPort.update(width, height, true)
    cam.position.set(0, 0, 0)
    cam.update()
  }

}
