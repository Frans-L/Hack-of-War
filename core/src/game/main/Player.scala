package game.main

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.{GameElement, Ticker}
import game.objects.GameObject

import scala.collection.mutable

/**
  * Created by Frans on 06/03/2018.
  */
class Player(ticker: Ticker, map: Map) extends GameElement {

  private val deck: mutable.Buffer[Card] = mutable.Buffer[Card]()
  val hand: mutable.Buffer[Card] = mutable.Buffer[Card]()

  hand.append(new Card(this))
  hand.append(new Card(this))
  hand.append(new Card(this))
  hand.append(new Card(this))


  override def update(): Unit = {

    //remove used cards
    for (i <- hand.indices.reverse)
      if (hand(i).used) hand.remove(i)


  }

  //returns true is succeeded
  def useCard(card: Card, posX: Float, posY: Float): Boolean = {

    //the card can be used
    if (!map.collide(posX, posY)) {
      true
    } else {
      false
    }
  }

  override def draw(shapeRender: ShapeRenderer): Unit = ???

  override def draw(batch: Batch): Unit = ???
}
