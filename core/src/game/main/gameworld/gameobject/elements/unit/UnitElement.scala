package game.main.gameworld.gameobject.elements.unit

import game.main.gameworld.gameobject
import game.main.gameworld.gameobject.{ObjectElement, objects}

trait UnitElement extends ObjectElement {

  /** Throws an error if the parent is not valid! */
  override def checkParent(parent: gameobject.GameObject): Unit =
    require(parent.isInstanceOf[objects.UnitObject], "Parent have to be UnitObject")
}
