package game.ui

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.{Actor, Touchable}

/**
  * Created by Frans on 28/02/2018.
  */
class UICard(shapeRenderer: ShapeRenderer) extends Actor {

  override def draw(batch: Batch, fl: Float): Unit = {

    shapeRenderer.rect(getX, getY, getWidth, getHeight)

  }

}
