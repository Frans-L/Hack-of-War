package game.ui.listener

import com.badlogic.gdx.math.Vector3

/**
  * Created by Frans on 27/02/2018.
  */
trait ButtonListener {

  def touchUp(touchPos: Vector3, pointer: Int, button: Int): Boolean

  def touchDown(touchPos: Vector3, pointer: Int, button: Int): Boolean

}
