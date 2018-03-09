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

  var startPos: Vector2 = new Vector2(0, 0) //tells the spot to stay when not moved

  private var dragPos: Vector2 = new Vector2(0, 0) //the pos which were touched

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


  //animation when the card is used
  def startUseAnim(x: Float, y: Float, finish: () => Unit): Unit = {
    val animTime = 400 / 1000f

    this.setTouchable(Touchable.disabled) //disable touch actions

    val anim = Actions.sequence(
      Actions.parallel(
        Actions.moveTo(x, y,
          animTime, Interpolation.pow2In),
        Actions.scaleTo(0, 0,
          animTime, Interpolation.pow2In)),
      Actions.run(new Runnable {
        override def run(): Unit = finish()
      }))
    this.addAction(anim)
  }

  //called when drag starts
  def startDrag(x: Float, y: Float): Unit = {

    val animTime = 200 / 1000f
    val scale = 0.5f

    dragPos = new Vector2(getWidth / 2f, getHeight / 2f)

    val anim = Actions.parallel(
      Actions.scaleTo(scale, scale, animTime, Interpolation.pow2),
      Actions.moveBy(
        x - getWidth * scale / 2f,
        y - getHeight * scale / 2f, animTime, Interpolation.pow2))
    addAction(anim)
  }

  //updates sprite pos
  def drag(x: Float, y: Float): Unit = {

    this.setPosition(
      this.getX + (x - dragPos.x) * getScaleX,
      this.getY + (y - dragPos.y) * getScaleY)
  }

  //stops the drag and returns final pos
  def stopDrag(x: Float, y: Float): (Float, Float) = {
    this.setPosition(
      this.getX + (x - dragPos.x),
      this.getY + (y - dragPos.y))

    (this.getX + dragPos.x * getScaleX, this.getY + dragPos.y * getScaleY)
  }


  //returns to the original spot with animation
  def backToStart(): Unit = {
    val animTime = 500 / 1000f

    this.setTouchable(Touchable.disabled) //disable touch actions

    val anim = Actions.sequence(
      Actions.parallel(
        Actions.moveTo(startPos.x, startPos.y, animTime, Interpolation.pow2In),
        Actions.scaleTo(1f, 1f, animTime, Interpolation.pow2In)),
      Actions.run(new Runnable {
        override def run(): Unit = setTouchable(Touchable.enabled) //disable touch actions
      }))
    this.addAction(anim)
  }


  //is called every frame by stage (calls method update)
  override def act(delta: Float): Unit = {
    super.act(ticker.delta / 1000f)
    this.update()
  }

}
