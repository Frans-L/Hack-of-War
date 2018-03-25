package game.main

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import game.loader.GameTextures
import game.main.physics.CollisionHandler
import game.GameElement
import game.main.physics.objects.UnitObject
import game.main.physics.objects.units.Soldier

import scala.collection.mutable

/**
  * Created by Frans on 06/03/2018.
  */
class Player(textures: GameTextures, collHandler: CollisionHandler, index: Int) extends GameElement {

  private val deck: mutable.Buffer[Card] = mutable.Buffer[Card]()
  val hand: mutable.Buffer[Card] = mutable.Buffer[Card]()


  val units: mutable.Buffer[UnitObject] = mutable.Buffer[UnitObject]()


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

      if (units(i).deleted) units.remove(i)
    }

  }

  /** Returns true is succeeded */
  def useCard(card: Card, posX: Float, posY: Float): Boolean = {

    //the card can be used
    if (!collHandler.map.collide(posX, posY)) {
      true
    } else {
      false
    }
  }

  def spawnUnit(x: Float, y: Float): Unit = {
    units += Soldier.create(collHandler, textures, x, y, MathUtils.random(0, 1))
  }

  override def draw(shapeRender: ShapeRenderer): Unit = {
    units.foreach(_.draw(shapeRender)) //for a debug at the moment
  }

  override def draw(batch: Batch): Unit = {
    units.foreach(_.draw(batch)) //draw units
  }
}
