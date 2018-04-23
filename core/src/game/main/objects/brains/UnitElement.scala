package game.main.objects.brains

import game.GameElement
import game.main.objects.improved.{ObjectElement, UnitObject}

trait UnitElement extends ObjectElement {

  var pUnit: UnitObject = _

}
