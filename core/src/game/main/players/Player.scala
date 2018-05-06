package game.main.players

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import game.GameElement
import game.main.cards.{Card, UnitCard}
import game.main.gameworld.gamemap
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.ObjectHandler
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.unitcreators._
import game.main.unitcreators.units._

import scala.collection.mutable
import scala.reflect.ClassTag

object Player {

  val maxMana: Float = 100f
  val manaSpeed: Float = 5f / 1000f

}

/**
  * Created by Frans on 06/03/2018.
  */
abstract class Player(val objectHandler: ObjectHandler, index: Int)
  extends GameElement {

  val colorIndex: Int //color of the textures

  //stats
  var mana: Float = 50f
  var manaSpeed: Float = Player.manaSpeed
  var cardsInHand: Int = 3
  var score: Int = 3 //the amount of buildings alive is the score

  //cards
  val deck: mutable.Buffer[Card] = mutable.Buffer[Card]()
  val hand: mutable.Buffer[Card] = mutable.Buffer[Card]()

  //the enemy of this player
  val enemies: mutable.Buffer[Player] = mutable.Buffer.empty[Player]

  //when the player is dragging card, the player might see the iconPath of the unit
  var iconPath: Option[gamemap.IconPath] = None

  //paths to spawn units
  protected val turretPaths: Seq[Path] //0 -> main, 1 -> down, 2 -> up
  protected val paths: Seq[Path] //0 - down, 1 -> up

  /** */
  protected def initialize(): Unit = {

    //created the turrets
    val main = spawnUnit(BuildingMain, 0, 0, Some(turretPaths(0))).head //main buildings
    val down = spawnUnit(BuildingLane, 0, 0, Some(turretPaths(1))).head //lanes
    val up = spawnUnit(BuildingLane, 0, 0, Some(turretPaths(2))).head
    main.addDeleteAction(() => score -= 3)
    down.addDeleteAction(() => score -= 1)
    up.addDeleteAction(() => score -= 1)

    //add cards to deck
    val basicAmount = 3
    val swarm1Amount, swarm3Amount, swarm5Amount = 1
    val tankAmount, shooterAmount = 2

    for (i <- 0 until basicAmount) deck += new UnitCard(this, SoldierBasic)
    for (i <- 0 until swarm1Amount) deck += new UnitCard(this, SoldierSwarm1)
    for (i <- 0 until swarm3Amount) deck += new UnitCard(this, SoldierSwarm3)
    for (i <- 0 until swarm5Amount) deck += new UnitCard(this, SoldierSwarm5)
    for (i <- 0 until tankAmount) deck += new UnitCard(this, TankBasic)
    for (i <- 0 until shooterAmount) deck += new UnitCard(this, SoldierShooter)

    for (i <- 0 until cardsInHand) drawCard()
  }

  /**
    * Updates cards and mana.
    **/
  override def update(): Unit = {

    mana = math.min(Player.maxMana, mana + manaSpeed * ticker.delta) //increase mana
    iconPath.foreach(i => i.update()) //update iconPath if it exists

    //remove used cards
    for (i <- hand.indices.reverse)
      if (hand(i).used) {
        hand.remove(i)
        drawCard()
      }

  }

  override def draw(shapeRender: ShapeRenderer): Unit = Unit

  override def draw(batch: Batch): Unit = {
    iconPath.foreach(i => {
      i.draw(batch)
    })
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
    } else false
  }

  /** Returns random card from hand. */
  def randomCard[T <: Card : ClassTag](): Option[T] = {
    val classTag = implicitly[ClassTag[T]].runtimeClass
    val h = hand.filter(classTag.isInstance)
    if (h.nonEmpty) Some(hand(MathUtils.random(h.size - 1)).asInstanceOf[T])
    else None
  }


  /** Spawns a new unit */
  def spawnUnit(unitCreator: UnitCreator, x: Float, y: Float, path: Option[Path], randomOffset: Boolean = false): Seq[UnitObject] = {
    val p = path.getOrElse(randomPath)
    val extraOffset = if (randomOffset) p.randomOffset else 0
    unitCreator.create(this, x, y, p, extraOffset)
  }

  /** Spawns a new unit. This method is called when forceUse to card is called. */
  def forceSpawnUnit(unitCreator: UnitCreator, x: Float, y: Float, path: Option[Path], randomOffset: Boolean = false): Seq[UnitObject] =
    spawnUnit(unitCreator, x, y, path, randomOffset)


  /** Returns the closest path from the map. */
  def findPath(x: Float, y: Float): Option[Path] = objectHandler.collHandler.map.getPath(x, y)

  /** Returns random path */
  def randomPath: Path = randomPathOption.getOrElse(new Path(Seq.empty, 0))

  /** Returns random path wrapped in Option */
  def randomPathOption: Option[Path] = paths.lift(MathUtils.random(paths.length - 1))

  /** Returns enemies buffer[Player] as buffer[GameElement] */
  def enemiesAsGameElement: mutable.Buffer[GameElement] = enemies.asInstanceOf[mutable.Buffer[GameElement]]

}
