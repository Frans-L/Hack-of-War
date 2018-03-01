package game.ui

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.{Gdx, InputProcessor}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.{InputEvent, InputListener, Stage, Touchable}
import com.badlogic.gdx.scenes.scene2d.utils.DragListener
import com.badlogic.gdx.utils.viewport.{ExtendViewport, Viewport}
import game.loader.GameTextures
import game.{GameElement, Ticker, World}

/**
  * Created by Frans on 28/02/2018.
  */
class GameUI(ticker: Ticker, gameTextures: GameTextures, world: World, viewport: Viewport, shapeRenderer: ShapeRenderer) extends GameElement {

  private val stage: Stage = new Stage(viewport)
  private val card: UICard = SoldierCard.create(gameTextures, shapeRenderer)
  card.setPosition(10, 10)
  card.setBounds(0, world.down, 150, 225)
  card.setTouchable(Touchable.enabled)
  card.updateSprite()

  stage.addActor(card)

  Gdx.input.setInputProcessor(stage)

  override def update(): Unit = ???

  override def draw(shapeRender: ShapeRenderer): Unit = {
    stage.draw()
  }

  def inputProcessor = stage

  override def draw(batch: Batch): Unit = {
    stage.draw() //stage has its own sprite batch
  }
}
