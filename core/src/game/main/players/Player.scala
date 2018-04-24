package game.main.players

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.{Pool, Pools}
import game.GameElement
import game.main.cards.Card
import game.main.gameMap.{IconPath, Path}
import game.main.objects.improved.ObjectHandler
import game.main.objects.improved.ObjectHandler.Level
import game.main.objects.{UnitObject, UnitPath}
import game.main.physics.CollisionHandler
import game.main.units.UnitCreator

import scala.collection.mutable

/**
  * Created by Frans on 06/03/2018.
  */
abstract class Player(val objectHandler: ObjectHandler, index: Int)
  extends GameElement {

  //when the player is dragging card, the player might see the iconPath of the unit
  var iconPath: Option[IconPath] = None

  val colorIndex: Int //color of the textures
  val enemies: mutable.Buffer[Player] = mutable.Buffer.empty[Player] //the enemy of this player

  private val deck: mutable.Buffer[Card] = mutable.Buffer[Card]()
  val hand: mutable.Buffer[Card] = mutable.Buffer[Card]()

  override def update(): Unit = {

    //update iconPath if it exists
    iconPath.foreach(i => i.update())

    //remove used cards
    for (i <- hand.indices.reverse)
      if (hand(i).used) hand.remove(i)

  }

  /** Returns true is succeeded */
  def useCard(card: Card, posX: Float, posY: Float): Boolean = {
    //the card can be used
    if (!objectHandler.collHandler.map.collide(posX, posY)) {
      true
    } else {
      false
    }
  }

  /** Spawns a new unit */
  def spawnUnit(unitCreator: UnitCreator, x: Float, y: Float,
                path: Path, random: Boolean = false): Unit = {
    unitCreator.create(this, x, y, path, random)
  }

  /** Returns the closes path from the map. */
  def findPath(x: Float, y: Float): Option[Path] = {
    objectHandler.collHandler.map.getPath(x, y)
  }

  override def draw(shapeRender: ShapeRenderer): Unit = Unit


  override def draw(batch: Batch): Unit = {
    iconPath.foreach(i => {
      i.draw(batch)
    })
  }

  /** Returns enemies buffer[Player] as buffer[GameElement] */
  def enemiesAsGameElement: mutable.Buffer[GameElement] = enemies.asInstanceOf[mutable.Buffer[GameElement]]

}
