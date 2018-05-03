package game.main.gameworld.gameobject.elements.unit

import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject
import game.main.gameworld.gameobject.{ObjectElement, objects}

/** Updates the unitObject's moveTarget. */
class FollowPath(var path: Path, val acceptDist: Float) extends UnitElement {

  private var targetI: Int = 0 //current target index

  override def update(p: gameobject.GameObject, delta: Int): Unit = {
    val parent = p.asInstanceOf[objects.UnitObject]

    //if at the checkpoint, get next checkpoint
    if (parent.pos.dst2(path(targetI)) <= acceptDist * acceptDist) {
      targetI = math.min(targetI + 1, path.length - 1)
    }

    parent.moveTarget.set(path(targetI))
  }

}
