package game.UI

import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
  * Created by Frans on 27/02/2018.
  */
class UICard(var posX: Double, var posY: Double, var width: Double, var height: Double) extends GameUIElement {

  override def update(): Unit = ???

  override def draw(shapeRender: ShapeRenderer): Unit = {
    if (visible) {

      shapeRender.rect(posX.toInt, posY.toInt, width.toInt, height.toInt)
    }
  }
}
