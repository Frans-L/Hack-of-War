package game.main.cards

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import game.main.gameMap.{IconPath, Path}
import game.main.objects.UnitPath
import game.main.players.Player
import game.main.units.Soldier

class UnitCard(owner: Player) extends Card(owner) {

  var unitPath: IconPath = new IconPath(
    new Path(Seq[Vector2](new Vector2(0, 0)), 0),
    Soldier.pathIcon(owner))

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
    owner.spawnUnit(x, y, unitPath.getOriginalPath)
    super.action(x, y)
  }

  override def destroy(): Unit = {
    super.destroy()
    owner.hand += new UnitCard(owner) //new card to the player
  }

}
