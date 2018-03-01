package game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.{Vector2, Vector3}
import com.badlogic.gdx.scenes.scene2d.{Actor, InputEvent, InputListener, Touchable}
import game.loader.GameTextures

/**
  * Created by Frans on 28/02/2018.
  */
class UICard(sprite: Sprite, shapeRenderer: ShapeRenderer) extends Actor {

  private var dragPos: Vector3 = new Vector3(0, 0, 0)

  this.addListener(new InputListener() {
    override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean = {
      startDrag(x, y)
      true
    }

    override def touchDragged(event: InputEvent, x: Float, y: Float, pointer: Int): Unit = {
      drag(x, y)
    }

    override def touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Unit = {
      Gdx.app.log("ylös", "x: " + x + " y:" + y)
    }
  })

  def updateSprite(): Unit = {
    sprite.setBounds(getX, getY, getWidth, getHeight)
  }

  override def draw(batch: Batch, fl: Float): Unit = {

    sprite.draw(batch)
    shapeRenderer.rect(getX, getY, getWidth, getHeight)

  }

  private def startDrag(x: Float, y: Float): Unit = {
    dragPos = new Vector3(x, y, 0)
  }

  private def drag(x: Float, y: Float): Unit = {
    this.setPosition(getX + x - dragPos.x, getY + y - dragPos.y)
    updateSprite()
  }


}
