package game.main

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.loader.GameTextures
import game.{GameElement, Ticker}
import game.objects.{ActiveObject, GameObject, Soldier}

import scala.collection.mutable

/**
  * Created by Frans on 06/03/2018.
  */
class Player(ticker: Ticker, textures: GameTextures, collDetect: CollisionDetector) extends GameElement {

  private val deck: mutable.Buffer[Card] = mutable.Buffer[Card]()
  val hand: mutable.Buffer[Card] = mutable.Buffer[Card]()


  val units: mutable.Buffer[ActiveObject] = mutable.Buffer[ActiveObject]()


  hand.append(new Card(this))
  hand.append(new Card(this))
  hand.append(new Card(this))
  hand.append(new Card(this))


  override def update(): Unit = {

    //remove used cards
    for (i <- hand.indices.reverse)
      if (hand(i).used) hand.remove(i)

    for (i <- units.indices.reverse) {
      units(i).update()

      if (units(i).removed) units.remove(i)
    }

  }

  //returns true is succeeded
  def useCard(card: Card, posX: Float, posY: Float): Boolean = {

    //the card can be used
    if (!collDetect.map.collide(posX, posY)) {
      true
    } else {
      false
    }
  }

  def spawnUnit(x: Float, y: Float): Unit = {
    units += Soldier.create(ticker, textures, collDetect, x, y)
  }

  override def draw(shapeRender: ShapeRenderer): Unit = ???

  override def draw(batch: Batch): Unit = {
    units.foreach(_.draw(batch)) //draw units
  }
}
