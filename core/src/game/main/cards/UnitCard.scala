package game.main.cards

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import game.main.gameworld.gamemap
import game.main.gameworld.gamemap.Path
import game.main.players.Player
import game.main.units.{BasicSoldier, SoldierCreator, UnitCreator}

class UnitCard(owner: Player, unitCreator: UnitCreator) extends Card(owner) {

  override val icon: Sprite = unitCreator.cardIcon(owner)
  override val cost: Int = unitCreator.cost

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
    owner.spawnUnit(BasicSoldier, x, y, unitPath.getOrgPath)
    super.action(x, y)
  }

  override def destroy(): Unit = {
    super.destroy()
    owner.hand += new UnitCard(owner, BasicSoldier) //new card to the player
  }

}
