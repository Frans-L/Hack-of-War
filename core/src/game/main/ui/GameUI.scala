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
import game.util.{ProgressTimer, Dimensions}


/**
  * Created by Frans on 28/02/2018.
  */
class GameUI(dimensions: Dimensions, viewport: Viewport,
             player: Player, shapeRenderer: ShapeRenderer,
             gameTimer: ProgressTimer) extends GameElement {

  private val stage: Stage = new Stage(viewport)
  private val manaBar: ManaBar = new ManaBar(player)
  private val uiTimer: UITimer = new UITimer(gameTimer)
  stage.addActor(manaBar)
  stage.addActor(uiTimer)

  Gdx.input.setInputProcessor(stage)

  //updates all element to the correct pos before drawing anything
  forceUpdate()

  override def update(): Unit = {

    //if a new card in a hand
    var organizeCard = false
    player.hand.foreach(c => {
      if (!c.uiExists) {
        val card = c.uiCreate()
        card.setBounds(0, dimensions.down, 170, 270)
        card.setScale(0f)
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

  /** Updates cards immediately. */
  def forceUpdate(): Unit = {
    this.organizeCards()

    //some magic coordinates numbers, should be named someday
    manaBar.setPosition(dimensions.left + 105, this.adjustedDownY + 40)
    uiTimer.setPosition(0, dimensions.up - 115)
  }


  /** Sets the cards to move right position. */
  private def organizeCards(): Unit = {
    val y = this.adjustedDownY
    val width = 170 + 10
    val cardAmount = player.hand.filter(_.uiExists).count(_.uiElement.get.hasIdlePlace)
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


  /** Shows the start splash screen */
  def showStartSplashScreen(action: () => Unit): Unit = {
    val splashScreen: SplashScreen =
      new SplashScreen("READY", dimensions.maxWidth, dimensions.maxHeight, action)
    stage.addActor(splashScreen)
  }

  //temporary solution
  private var endScreenOn: Boolean = false

  /** Shows the end splash screen. */
  def showEndSplashScreen(result: String, endAction: () => Unit): Unit = {
    if (!endScreenOn) {
      val splashScreen: SplashScreen =
        new SplashScreen(result, dimensions.maxWidth, dimensions.maxHeight, endAction)
      stage.addActor(splashScreen)
    }
    endScreenOn = true
  }

  /** Returns "average" the y-pos of the screen resolution. */
  private def adjustedDownY: Float = (dimensions.down - viewport.getWorldHeight / 2) / 2

  override def draw(shapeRender: ShapeRenderer): Unit = Unit

  /** Needed if have to combine different inputProcessors. Currently no need. */
  def inputProcessor: Stage = stage

  override def draw(batch: Batch): Unit = {
    stage.draw() //stage has its own sprite batch
  }
}
