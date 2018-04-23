package game.main.objects.brains

import com.badlogic.gdx.Gdx
import game.main.gameMap.{IconPath, Path}
import game.main.objects.UnitObject

/** Updates the unitObject's moveTarget. */
class FollowPath(var path: Path, val acceptDist: Float) extends UnitElement {

  Gdx.app.log(this.toString, "hei")

  private var targetI: Int = 0 //current target index

  override def update(delta: Int): Unit = {

    //if at the checkpoint, get next checkpoint
    if (pUnit.pos.dst2(path(targetI)) <= acceptDist * acceptDist) {
        targetI = math.min(targetI + 1, path.length - 1)
    }

    pUnit.moveTarget.set(path(targetI))
  }

}
