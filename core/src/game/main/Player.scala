package game.main

import com.badlogic.gdx.Gdx
import game.Ticker

import scala.collection.mutable

/**
  * Created by Frans on 06/03/2018.
  */
class Player(ticker: Ticker, map: Map) {

  private val deck: mutable.Buffer[Card] = mutable.Buffer[Card]()
  val hand: mutable.Buffer[Card] = mutable.Buffer[Card]()

  hand.append(new Card(this))


  //returns true is succeeded
  def useCard(card: Card, posX: Float, posY: Float): Boolean = {

    //the card can be used
    if (!map.collide(posX, posY)) {
      true
    } else {
      false
    }
  }

}
