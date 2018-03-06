package game.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.{Interpolation, Vector2, Vector3}
import com.badlogic.gdx.scenes.scene2d.actions.{Actions, MoveToAction, ParallelAction, ScaleByAction}
import com.badlogic.gdx.scenes.scene2d._
import game.Ticker
import game.loader.GameTextures


object UICard {

  val easUse: Interpolation = Interpolation.pow2In
  val easUseDur: Long = 500 //ms

}

/**
  * Created by Frans on 28/02/2018.
  */
class UICard(ticker: Ticker, sprite: Sprite) extends Actor {

  private var dragPos: Vector2 = new Vector2(0, 0)

  def updateSprite(): Unit = {
    sprite.setBounds(
      this.getX,
      this.getY,
      this.getWidth * this.getScaleX,
      this.getHeight * this.getScaleY)
  }

  def update(): Unit = {
    updateSprite()
  }

  override def draw(batch: Batch, fl: Float): Unit = {
    sprite.draw(batch)
    //shapeRenderer.rect(getX, getY, getWidth, getHeight)

  }

  def startUseAnim(finish: () => Unit): Unit = {
    val animTime = 500 / 1000f

    val anim = Actions.sequence(
      Actions.parallel(
        Actions.moveTo(this.getX + getWidth / 2, this.getY + getHeight / 2, animTime),
        Actions.scaleTo(0, 0, animTime)),
      Actions.run(new Runnable {
        override def run(): Unit = finish()
      }))
    this.addAction(anim)
  }

  //called when drag starts
  def startDrag(x: Float, y: Float): Unit = {
    dragPos = new Vector2(x, y)
  }

  //updates sprite pos
  def drag(x: Float, y: Float): Unit = {
    this.setPosition(this.getX + x - dragPos.x, this.getY + y - dragPos.y)
  }

  //stops the drag and returns final pos
  def stopDrag(x: Float, y: Float): (Float, Float) = {
    this.setPosition(this.getX + x - dragPos.x, this.getY + y - dragPos.y)
    (this.getX, this.getY)
  }


  //is called every frame by stage (calls method update)
  override def act(delta: Float): Unit = {
    super.act(ticker.delta / 1000f)
    this.update()
  }

}
