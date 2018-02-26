package game

/**
  * Created by Frans on 26/02/2018.
  */
trait GameObject extends GameElement {

  var posX: Double
  var posY: Double
  var visibile: Boolean = true
  var enabled: Boolean = true


}
