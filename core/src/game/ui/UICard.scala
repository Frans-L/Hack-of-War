package game.ui

import com.badlogic.gdx.{Application, Gdx}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import game.ui.listener.{ButtonListener, CardListener}


/**
  * Created by Frans on 27/02/2018.
  */
class UICard(var posX: Double, var posY: Double, var width: Double, var height: Double) extends GameUIElement with CardListener {

  override def update(): Unit = ???

  override def draw(shapeRender: ShapeRenderer): Unit = {
    if (visible) {

      shapeRender.rect(posX.toInt, posY.toInt, width.toInt, height.toInt)
    }
  }

  def touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = ???

  override def touchUp(touchPos: Vector3, pointer: Int, button: Int): Boolean = {
    Gdx.app.log("touchUp", "x: " + touchPos.x + " y: " + touchPos.y)
    false
  }

  override def touchDown(touchPos: Vector3, pointer: Int, button: Int): Boolean = {
    Gdx.app.log("touchDown", "x: " + touchPos.x + " y: " + touchPos.y)
    false
  }
}
