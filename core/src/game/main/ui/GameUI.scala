package game.main.ui

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.{Gdx, InputProcessor}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.{InputEvent, InputListener, Stage, Touchable}
import com.badlogic.gdx.scenes.scene2d.utils.DragListener
import com.badlogic.gdx.utils.viewport.{ExtendViewport, Viewport}
import game.loader.GameTextures
import game.GameElement
import game.main.players.Player
import game.util.Dimensions


/**
  * Created by Frans on 28/02/2018.
  */
class GameUI(dimensions: Dimensions, viewport: Viewport,
             player: Player,
             shapeRenderer: ShapeRenderer) extends GameElement {

  private val stage: Stage = new Stage(viewport)

  Gdx.input.setInputProcessor(stage)

  override def update(): Unit = {

    //if a new card in a hand
    var organizeCard = false
    player.hand.foreach(c => {
      if (!c.uiExists) {
        val card = c.uiCreate()
        card.setBounds(0, dimensions.down, 170, 270)
        card.setScale(0f)
        card.setTouchable(Touchable.enabled)
        card.updateSprites()
        stage.addActor(card)
        card.toBack()
        organizeCard = true
      } else if (c.uiElement.get.isMoved()) {
        organizeCard = true
      }
    })

    if (organizeCard) this.organizeCards()


    //updates stage elements
    stage.act(ticker.delta / 1000f)

  }

  private def organizeCards(): Unit = {

    val y = dimensions.down
    val width = 170 + 10

    val cardAmount = player.hand.filter(_.uiExists).count(_.uiElement.get.hasIdlePlace)
    Gdx.app.log("GAMEUI", "cardAmount: " + cardAmount)

    if (cardAmount > 0) {
      val shift = (cardAmount + 1) % 2 / 2f
      var i = -cardAmount / 2
      player.hand.foreach(c => {
        if (c.uiExists) {
          val card = c.uiElement.get
          if (card.hasIdlePlace) {
            card.setStartPosition((i + shift) * width - width / 2f, y)
            i += 1
          }
        }
      })
    }


  }

  override def draw(shapeRender: ShapeRenderer): Unit = {
    stage.draw()
  }

  def inputProcessor = stage

  override def draw(batch: Batch): Unit = {
    stage.draw() //stage has its own sprite batch
  }
}
