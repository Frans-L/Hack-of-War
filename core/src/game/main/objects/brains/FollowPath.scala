package game.main.objects.brains

import com.badlogic.gdx.Gdx
import game.main.gameMap.{IconPath, Path}
import game.main.objects.improved.{GameObject, ObjectElement, UnitObject}

/** Updates the unitObject's moveTarget. */
class FollowPath(var path: Path, val acceptDist: Float) extends ObjectElement {

  private var targetI: Int = 0 //current target index

  override def update(p: GameObject, delta: Int): Unit = {
    val parent = p.asInstanceOf[UnitObject]

    //if at the checkpoint, get next checkpoint
    if (parent.pos.dst2(path(targetI)) <= acceptDist * acceptDist) {
      targetI = math.min(targetI + 1, path.length - 1)
    }

    parent.moveTarget.set(path(targetI))
  }

  /** Throws an error if the parent is not valid! */
  override def checkParent(parent: GameObject): Unit =
    require(parent.isInstanceOf[UnitObject], "Parent have to be UnitObject")

}
