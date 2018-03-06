package game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.{Vector2, Vector3}
import com.badlogic.gdx.scenes.scene2d.{Actor, InputEvent, InputListener, Touchable}
import game.Ticker
import game.loader.GameTextures

/**
  * Created by Frans on 28/02/2018.
  */
class UICard(sprite: Sprite) extends Actor {

  private var dragPos: Vector2 = new Vector2(0, 0)

  def updateSprite(): Unit = {
    sprite.setBounds(this.getX, this.getY, getWidth, getHeight)
  }

  override def draw(batch: Batch, fl: Float): Unit = {

    sprite.draw(batch)
    //shapeRenderer.rect(getX, getY, getWidth, getHeight)

  }

  //called when drag starts
  def startDrag(x: Float, y: Float): Unit = {
    dragPos = new Vector2(x, y)
  }

  //updates sprite pos
  def drag(x: Float, y: Float): Unit = {
    this.setPosition(this.getX + x - dragPos.x, this.getY + y - dragPos.y)
    updateSprite()
  }

  def stopDrag(x: Float, y: Float): (Float, Float) = {
    this.setPosition(this.getX + x - dragPos.x, this.getY + y - dragPos.y)
    updateSprite()
    (this.getX, this.getY)
  }

}
