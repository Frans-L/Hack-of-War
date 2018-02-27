package game.ui.listener

/**
  * Created by Frans on 27/02/2018.
  */
trait DragListener {
  def touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean
}
