package game.main.players

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import game.GameElement
import game.loader.GameTextures.Units.BaseSoldier
import game.main.cards.{Card, UnitCard}
import game.main.gameworld.gamemap
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.ObjectHandler
import game.main.units
import game.main.units._

import scala.collection.mutable

object Player {

  val maxMana: Float = 100f
  val manaSpeed: Float = 10f / 1000f

}

/**
  * Created by Frans on 06/03/2018.
  */
abstract class Player(val objectHandler: ObjectHandler, index: Int)
  extends GameElement {

  var mana: Float = 50f
  var manaSpeed: Float = Player.manaSpeed

  //when the player is dragging card, the player might see the iconPath of the unit
  var iconPath: Option[gamemap.IconPath] = None

  val colorIndex: Int //color of the textures
  val enemies: mutable.Buffer[Player] = mutable.Buffer.empty[Player] //the enemy of this player

  private val deck: mutable.Buffer[Card] = mutable.Buffer[Card]()
  val hand: mutable.Buffer[Card] = mutable.Buffer[Card]()

  var cardsInHand: Int = 3


  protected def initialize(): Unit = {
    deck += new UnitCard(this, BasicSoldier)
    deck += new UnitCard(this, SwarmSoldier3)
    deck += new UnitCard(this, SwarmSoldier5)

    for (i <- 0 until cardsInHand) drawCard()
  }

  override def update(): Unit = {

    mana = math.min(Player.maxMana, mana + manaSpeed * ticker.delta) //increase mana

    //update iconPath if it exists
    iconPath.foreach(i => i.update())

    //remove used cards
    for (i <- hand.indices.reverse)
      if (hand(i).used) {
        hand.remove(i)
        drawCard()
      }

  }

  /** Draws a new card form the deck to hand. */
  def drawCard(): Card = {
    val i = MathUtils.random(deck.size - 1)
    val card = deck(i).cpy()
    hand += card //adds to hand
    card
  }

  /** Returns true is succeeded. */
  def useCard(card: Card, posX: Float, posY: Float): Boolean = {
    //the card can be used
    if (card.cost <= mana && !objectHandler.collHandler.map.collide(posX, posY)) {
      mana -= card.cost
      true
    } else {
      false
    }
  }

  /** Returns random card from hand. */
  def randomCard[T <: Card](): Option[T] = {
    val h = hand.filter(_.isInstanceOf[T])
    if(h.nonEmpty)
      Some(hand(MathUtils.random(h.size - 1)).asInstanceOf[T])
    else None
  }

  /** Spawns a new unit */
  def spawnUnit(unitCreator: UnitCreator, x: Float, y: Float,
                path: Path, random: Boolean = false): Unit = {
    val extraOffset = if (random) path.randomOffset else 0
    unitCreator.create(this, x, y, path, extraOffset)
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
