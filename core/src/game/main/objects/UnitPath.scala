package game.main.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite, SpriteBatch}
import com.badlogic.gdx.math.Vector2
import game.main.gameMap.{IconPath, Path}
import game.util.Vector2e
import game.util.Vector2e._



/** Tells the unit where to go next and can draw the path. */
class UnitPath(pathOrg: Path, icon: Sprite, offsetB: Float, val acceptDist: Float)
  extends IconPath(pathOrg, icon){

  val target: Vector2 = Vector2e(0, 0)
  private var targetI: Int = -1 //current target index

  this.setOffset(offsetB)
  updateTarget(target)

  /** Returns the pos of the steering target. */
  def updateTarget(pos: Vector2): Vector2 = {

    //if at the checkpoint, get next checkpoint
    if (targetI < path.length) {
      if (pos.dst2(target) <= acceptDist * acceptDist) {
        targetI = targetI + 1
        target.set(path(math.min(targetI, path.length - 1)))
      }
    } else {
      target.set(pos)
    }

    target
  }


}
