package game.main

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.{InputEvent, InputListener}
import game.loader.GameTextures
import game.main.ui.UICard

object Card {

  val background: String = GameTextures.Units.card1

}

/**
  * Created by Frans on 06/03/2018.
  */
class Card(owner: Player) {

  var used = false

  var uiElement: Option[UICard] = None

  //returns true if ui exists
  def uiExists: Boolean = {
    uiElement.isDefined
  }

  //creates ui element for the card
  def uiCreate(): UICard = {
    uiElement = Some(
      new UICard(GameTextures.defaultUITextures.atlas.createSprite(Card.background)))
    this.addListeners()
    uiElement.get
  }

  //adds listeners to UI element
  private def addListeners(): Unit = {
    require(uiElement.isDefined)
    val e = uiElement.get
    e.addListener(new InputListener() {
      override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean = {
        e.startDrag(x, y)
        true
      }

      override def touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int): Unit = {
        e.drag(x, y)
      }

      override def touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Unit = {
        val (x2, y2) = e.stopDrag(x, y)
        use(x2, y2)
      }

    })
  }

  //tries to use the card
  private def use(x: Float, y: Float): Unit = {
    if (owner.useCard(this, x, y)) {
      uiElement.foreach(_.startUseAnim(x, y, () => action(x, y)))
      Gdx.app.log("card", "used")
    } else {
      uiElement.foreach(_.backToStart()) //back to start pos
    }
  }

  //card's action that is called when the card is used
  private def action(x: Float, y: Float): Unit = {
    owner.spawnUnit(x, y)
    destroy()
  }


  def destroy(): Unit = {
    uiElement.foreach(_.remove()) //remove UI element
    used = true //mark that card can be destroyed
    owner.hand += new Card(owner) //new card to the player
  }

}
