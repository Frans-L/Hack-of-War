package game

import com.badlogic.gdx.Gdx._
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.{Application, Gdx, InputProcessor}
import game.ui.listener.{ButtonListener, CardListener, DragListener}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by Frans on 27/02/2018.
  */
class UIInputListener(cam: Camera) extends InputProcessor {

  val buttonListener: ArrayBuffer[ButtonListener] = new ArrayBuffer[ButtonListener]()
  val dragListener: ArrayBuffer[DragListener] = new ArrayBuffer[DragListener]()

  override def touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = {
    false
  }

  override def touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    val touchPos = cam.unproject(new Vector3(screenX, screenY, 0)) //finds real coords
    var break = false
    for (elem <- buttonListener if !break) //tries to touch every element
      break = elem.touchUp(touchPos, pointer, button)
    break //returns true, if the touch is "used"
  }

  override def touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = {
    val touchPos = cam.unproject(new Vector3(screenX, screenY, 0)) //finds real coords
    var break = false
    for (elem <- buttonListener if !break) //tries to touch every element
      break = elem.touchDown(touchPos, pointer, button)
    break //returns true, if the touch is "used"
  }

  override def keyUp(keycode: Int): Boolean = false

  override def scrolled(amount: Int): Boolean = false

  override def keyTyped(character: Char): Boolean = false

  override def keyDown(keycode: Int): Boolean = false

  override def mouseMoved(screenX: Int, screenY: Int): Boolean = false

}
