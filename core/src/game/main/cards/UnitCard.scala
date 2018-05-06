package game.main.cards

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import game.main.gameworld.gamemap
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.GameObject
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.players.Player
import game.main.unitcreators.{SoldierCreator, UnitCreator}

class UnitCard(owner: Player, val unitCreator: UnitCreator) extends Card(owner) {

  override val cost: Int = unitCreator.cost
  override val icon: GameObject = unitCreator.cardIcon(owner, cost)
  override val aiScore: UnitObject.AIScore = unitCreator.aiScore

  var unitPath: gamemap.IconPath = new gamemap.IconPath(
    new Path(Seq[Vector2](new Vector2(0, 0)), 0),
    unitCreator.pathIcon(owner))

  override def beforeUse(x: Float, y: Float): Unit = {
    val path = owner.findPath(x, y)
    if (path.isDefined) {
      unitPath.changePath(path.get) //update the path
      unitPath.setOffset(path.get.findOffset(x, y))
      unitPath.drawTimer.forward().start() //start animation
      owner.iconPath = Some(unitPath) //update players unitPath to be right one
    } else {
      unitPath.drawTimer.backward().start()
    }
  }

  override protected def action(x: Float, y: Float): Unit = {
    unitPath.drawTimer.backward().start()
    owner.spawnUnit(unitCreator, x, y, Some(unitPath.getOrgPath))
    super.action(x, y)
  }

  override protected def forceAction(x: Float, y: Float): Unit = {
    owner.forceSpawnUnit(unitCreator, x, y, None)
    super.forceAction(x, y)
  }

  override protected def actionFailed(x: Float, y: Float): Unit = {
    unitPath.drawTimer.backward().start()
    super.actionFailed(x, y)
  }


  override def destroy(): Unit = {
    super.destroy()
  }

  /** Returns a copy of the card. */
  override def cpy(): Card = {
    new UnitCard(owner, unitCreator)
  }

}
