package game.main.cards

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.{InputEvent, InputListener}
import game.loader.GameTextures
import game.main.gameworld.gameobject.GameObject
import game.main.gameworld.gameobject.objects.UnitObject.AIScore
import game.main.players.Player
import game.main.ui.UICard

object Card {

  val background: String = GameTextures.Units.card1

}

/**
  * Created by Frans on 06/03/2018.
  */
abstract class Card(owner: Player) {

  val icon: GameObject
  val cost: Int
  val aiScore: AIScore

  var used = false
  var uiElement: Option[UICard] = None

  /** Returns true if ui exists */
  def uiExists: Boolean = {
    uiElement.isDefined
  }

  /** Creates ui element for the card */
  def uiCreate(): UICard = {
    uiElement = Some(
      new UICard(
        GameTextures.defaultUI.atlas.createSprite(Card.background), icon, cost.toString))
    this.addListeners()
    uiElement.get
  }

  /** Adds listeners to UI element */
  private def addListeners(): Unit = {
    require(uiElement.isDefined)
    val e = uiElement.get
    e.addListener(new InputListener() {
      override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean = {
        e.startDrag(x, y)
        Card.this.selected(x, y)
        true
      }

      override def touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int): Unit = {
        val (x2, y2) = e.drag(x, y)
        Card.this.beforeUse(x2, y2)
      }

      override def touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Unit = {
        val (x2, y2) = e.stopDrag(x, y)
        Card.this.use(x2, y2)
      }

    })
  }

  /** Tries to use the card */
  def use(x: Float, y: Float): Unit = {
    if (usable(x, y)) {
      if(uiElement.isEmpty) action(x, y) //if no UI element, act immediately
      uiElement.foreach(_.startUseAnim(x, y, () => action(x, y)))
    } else {
      uiElement.foreach(_.backToStart()) //back to start pos
      actionFailed(x, y)
    }
  }

  /** Tries to use the card even if it wouldn't be accepted.
    * Useful when making AI. */
  def forceUse(x: Float, y: Float): Unit = {
    owner.useCard(this, x, y)
    if(uiElement.isEmpty) forceAction(x, y) //if no UI element, act immediately
    uiElement.foreach(_.startUseAnim(x, y, () => forceAction(x, y)))
  }

  /** The actions when the card is selected / drag is started. */
  protected def selected(x: Float, y: Float): Unit = Unit

  /** The actions when the card is dragged. */
  protected def beforeUse(x: Float, y: Float): Unit = Unit

  /** Returns true if the card can be used. */
  protected def usable(x: Float, y: Float): Boolean = {
    owner.useCard(this, x, y)
  }

  /** Card's action that is called when the card is used */
  protected def action(x: Float, y: Float): Unit = {
    destroy()
  }


  /** Card's action that is called when the card is forced to be used */
  protected def forceAction(x: Float, y: Float): Unit = {
    destroy()
  }

  /** Card's action that is called when the card cannot be used.
    * Will be called immediately when the action fails. */
  protected def actionFailed(x: Float, y: Float): Unit = Unit


  /** Destroys the card. */
  def destroy(): Unit = {
    uiElement.foreach(_.remove()) //remove UI element
    used = true //mark that card can be destroyed
  }

  /** Returns a copy of the card. */
  def cpy(): Card

}
