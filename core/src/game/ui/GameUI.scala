package game.ui

import com.badlogic.gdx.{Gdx, InputProcessor}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.{InputEvent, InputListener, Stage, Touchable}
import com.badlogic.gdx.scenes.scene2d.utils.DragListener
import com.badlogic.gdx.utils.viewport.{ExtendViewport, Viewport}
import game.{GameElement, Ticker, World}

/**
  * Created by Frans on 28/02/2018.
  */
class GameUI(ticker: Ticker, world: World, viewport: Viewport, shapeRenderer: ShapeRenderer) extends GameElement {

  private val stage: Stage = new Stage(viewport)
  private val card: UICard = new UICard(shapeRenderer)
  card.setPosition(10, 10)
  card.setBounds(10, 10, 100, 100)
  card.setTouchable(Touchable.enabled)

  card.addListener(new InputListener() {
    override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean = {
      Gdx.app.log("klikattu", "x: " + x + " y:" + y)
      true
    }
  })

  stage.addActor(card)

  Gdx.input.setInputProcessor(stage)

  override def update(): Unit = ???

  override def draw(shapeRender: ShapeRenderer): Unit = {
    stage.draw()
  }

  def inputProcessor = stage

}
