package game.main.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import game.main.gameMap.Path
import game.util.Vector2e
import game.util.Vector2e._

class UnitPath(val path: Path, val posAtPath: Vector2, val acceptDist: Float) {

  private var targetI: Int = -1 //current target index

  val target: Vector2 = Vector2e(0, 0)

  updateTarget(target)

  /** Returns the pos of the steering target. */
  def updateTarget(pos: Vector2): Vector2 = {

    //if at the checkpoint, get next checkpoint
    if (targetI < path.length) {
      if (pos.dst2(target) <= acceptDist * acceptDist) {
        targetI = targetI + 1
        target.set(path(math.min(targetI, path.length - 1))) ++ posAtPath
        Gdx.app.log("unitpath", "target: " + target)
      }
    } else {
      target.set(pos)
    }

    //Gdx.app.log("unitpath", "pos: " + pos)
    //Gdx.app.log("unitpath", "" + target)
    target
  }


}
