package game.main.players

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import game.main.cards.{Card, UnitCard}
import game.main.gameworld.gamemap
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.ObjectHandler
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.gameworld.gameobject.objects.UnitObject.AIScore
import game.main.unitcreators.UnitCreator
import game.main.unitcreators.units.{BuildingLane, BuildingMain, SoldierBasic}
import game.util.Ticker
import scala.collection.mutable

class Bot(objectHandler: ObjectHandler, override val colorIndex: Int) extends
  Player(objectHandler, colorIndex) {

  override protected val turretPaths: Seq[Path] = objectHandler.collHandler.map.turretPathReversed
  override protected val paths: Seq[Path] = objectHandler.collHandler.map.pathReversed

  private val map: gamemap.Map = objectHandler.collHandler.map

  /** When the AI should react fast */
  private val alertMapLine: Float = map.dimensions.right / 1.75f
  private var focusPath: Option[Path] = paths.headOption

  initialize()

  override def initialize(): Unit = {
    super.initialize()
  }

  override def update(): Unit = {
    super.update()

    if (ticker.interval2) {
      val units = objectHandler.updateObjects(ObjectHandler.Level.ground.id)
      val enemies =
        units.filter(u => u.isInstanceOf[UnitObject] && this.enemies.contains(u.asInstanceOf[UnitObject].owner))

      var priorityUp = mutable.Map[UnitObject.AIScore, Float](UnitObject.AIScoreNone -> 0)
      var priorityDown = mutable.Map[UnitObject.AIScore, Float](UnitObject.AIScoreNone -> 0)

      //find the most dangerous enemy
      enemies.foreach(e => {
        val enemy = e.asInstanceOf[UnitObject]
        if (enemy.pos.x > map.centerX) {

          val score = enemy.aiScore.priority * enemy.pos.x / alertMapLine
          if (enemy.pos.y > map.centerY)
            priorityUp(enemy.aiScore) = priorityUp.getOrElse(enemy.aiScore, 0f) + score
          else
            priorityDown(enemy.aiScore) = priorityUp.getOrElse(enemy.aiScore, 0f) + score
        }
      })

      //find scores, priority and path which to use
      val ((enemyScore, priority), pathIndex) = {
        val maxUp = priorityUp.maxBy(_._2)
        val maxDown = priorityDown.maxBy(_._2)
        if (maxUp._2 > maxDown._2) (maxUp, gamemap.Map.PathIndex.up)
        else (maxDown, gamemap.Map.PathIndex.down)
      }

      //update spawn path
      focusPath = if(priority > 0) paths.lift(pathIndex.id) else this.randomPathOption

      //if time to spawn units
      if (mana >= Player.maxMana || MathUtils.randomBoolean(priority / 100f)) {
        var handScored = hand.filter(_.cost <= mana).map(card => (card, sumAIScore(card.aiScore, enemyScore)))
        //spawn all units as possible
        while (handScored.nonEmpty) {
          val card = handScored.maxBy(_._2)._1.forceUse(0, 0)
          handScored = handScored.filter(s => s._1.cost <= mana && !s._1.used)
        }
      }
      
      //Debug printing
      Gdx.app.log("BOT", "Mana: " + mana + " Priority: " + priority)
      Gdx.app.log("BOT", "Hand: " + hand.map(c => c.aiScore.toString))
    }

  }

  /** Returns the score of the fight own vs enemy */
  private def sumAIScore(own: AIScore, enemy: AIScore): Float = {
    (own.attackHeavy * enemy.heavy + own.attackLight * enemy.light) -
      (enemy.attackHeavy * own.heavy + enemy.attackLight * own.light)
  }

  /** Spawns a new unit. This method is called when forceUse to card is called. */
  override def forceSpawnUnit(unitCreator: UnitCreator, x2: Float, y2: Float, path: Option[Path], random: Boolean = true): Seq[UnitObject] = {
    val x = focusPath.map(_.head.x).getOrElse(0f)
    val y = focusPath.map(_.head.y).getOrElse(0f)
    spawnUnit(unitCreator, x, y, focusPath, random)
  }

  /** Bot can always use cards as it wants. */
  override def useCard(card: Card, posX: Float, posY: Float): Boolean = {
    mana -= card.cost
    true
  }

  /** If need to play totally random. */
  private def randomPlay(): Unit = {
    for (i <- 0 to MathUtils.random(3)) {
      val path = objectHandler.collHandler.map.randomPathReversed
      randomCard[UnitCard]().foreach(card =>
        spawnUnit(card.unitCreator, path.head.x, path.head.y, Some(path), true))
    }

    /*
    //spawn also for the user
    for (i <- 0 to MathUtils.random(2)) {
      val path = objectHandler.collHandler.map.randomPath
      enemies.head.randomCard[UnitCard]().foreach(card =>
        enemies.head.spawnUnit(card.unitCreator, path.head.x, path.head.y, path, true))
    }
    */
  }


}
