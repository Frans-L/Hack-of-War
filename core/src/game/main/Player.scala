package game.main

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import game.loader.GameTextures
import game.main.physics.PhysicsWorld
import game.GameElement
import game.main.physics.objects.UnitObject
import game.main.physics.objects.units.Soldier

import scala.collection.mutable

/**
  * Created by Frans on 06/03/2018.
  */
class Player(textures: GameTextures, physWorld: PhysicsWorld, index: Int) extends GameElement {

  private val deck: mutable.Buffer[Card] = mutable.Buffer[Card]()
  val hand: mutable.Buffer[Card] = mutable.Buffer[Card]()


  initialize()

  protected def initialize(): Unit = {
    hand.append(new Card(this))
    hand.append(new Card(this))
    hand.append(new Card(this))
    hand.append(new Card(this))
  }


  override def update(): Unit = {

    //remove used cards
    for (i <- hand.indices.reverse)
      if (hand(i).used) hand.remove(i)

  }

  /** Returns true is succeeded */
  def useCard(card: Card, posX: Float, posY: Float): Boolean = {

    //the card can be used
    if (!physWorld.map.collide(posX, posY)) {
      true
    } else {
      false
    }
  }

  /** Spawns a new unit */
  def spawnUnit(x: Float, y: Float): Unit = {
    Soldier.create(textures, this, physWorld, x, y, index)
  }

  override def draw(shapeRender: ShapeRenderer): Unit = {
  }

  override def draw(batch: Batch): Unit = {
  }

}
