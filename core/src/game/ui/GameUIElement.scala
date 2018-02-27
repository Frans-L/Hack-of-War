package game.ui

import game.GameElement

/**
  * Created by Frans on 27/02/2018.
  */
trait GameUIElement extends GameElement {

  var posX: Double
  var posY: Double
  var visible: Boolean = true
  var enabled: Boolean = true

}

