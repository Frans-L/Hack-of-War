package game.GameObject

import game.GameElement

/**
  * Created by Frans on 26/02/2018.
  */
trait GameObject extends GameElement {

  var posX: Double
  var posY: Double
  var visible: Boolean = true
  var enabled: Boolean = true


}
