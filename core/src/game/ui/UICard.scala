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

  object State {
    val IDLE = 0
    val RETURN = 1
    val MOVE = 2
    val DRAG = 3
    val ACTION = 4
  }


}

/**
  * Created by Frans on 28/02/2018.
  */
class UICard(ticker: Ticker, sprite: Sprite) extends Actor {

  private var startPos: Vector2 = new Vector2(0, 0) //tells the spot to stay when not moved
  var state = UICard.State.IDLE
  private var moved = false

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

    state = UICard.State.ACTION
    this.setTouchable(Touchable.disabled) //disable touch actions

    val animTime = 400 / 1000f
    val anim = Actions.sequence(
      Actions.parallel(
        Actions.moveTo(x, y,
          animTime, Interpolation.circleOut),
        Actions.scaleTo(0, 0,
          animTime, Interpolation.circleOut)),
      Actions.run(new Runnable {
        override def run(): Unit = finish()
      }))
    this.addAction(anim)
  }

  //called when drag starts
  def startDrag(x: Float, y: Float): Unit = {

    if (state == UICard.State.MOVE ||
      state == UICard.State.RETURN) this.clearActions() //clears unimportant actions

    state = UICard.State.DRAG
    moved = true //marks that card is moved

    dragPos = new Vector2(getWidth / 2f, getHeight / 2f)
    this.toFront() //brings it front

    val animTime = 200 / 1000f
    val scale = 0.5f

    val anim = Actions.parallel(
      Actions.scaleTo(scale, scale, animTime, Interpolation.pow2Out),
      Actions.moveBy(
        x - getWidth * scale / 2f,
        y - getHeight * scale / 2f, animTime, Interpolation.pow2Out))
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

    moved = true //card is moved
    state = UICard.State.IDLE
    this.setPosition(
      this.getX + (x - dragPos.x) * getScaleX,
      this.getY + (y - dragPos.y) * getScaleY)

    (this.getX + dragPos.x * getScaleX, this.getY + dragPos.y * getScaleY)
  }


  //card returns to the original spot with animation
  def backToStart(): Unit = {

    state = UICard.State.RETURN
    val animTime = 400 / 1000f
    val anim = Actions.sequence(Actions.parallel(
      Actions.moveTo(startPos.x, startPos.y, animTime, Interpolation.pow2In),
      Actions.scaleTo(1f, 1f, animTime, Interpolation.pow2In)),
      Actions.run(new Runnable {
        override def run(): Unit = {
          state = UICard.State.IDLE
          updateStartPos()
        }
      }))

    this.addAction(anim)
  }

  //card moves to it original spot with fast animation
  private def updateStartPos(): Unit = {
    val animTime = 200 / 1000f

    val anim = Actions.parallel(
      Actions.moveTo(startPos.x, startPos.y, animTime, Interpolation.circle),
      Actions.scaleTo(1f, 1f, animTime, Interpolation.circle)
    )
    this.addAction(anim)
  }


  //sets the new start pos, and move card to it
  def setStartPosition(x: Float, y: Float): Unit = {
    startPos = new Vector2(x, y)
    if (state == UICard.State.IDLE)
      updateStartPos()

  }


  def hasIdlePlace: Boolean =
    (state == UICard.State.IDLE) || (state == UICard.State.RETURN)

  //TODO Rename this class
  def isMoved(): Boolean = {
    val r = moved
    moved = false
    r
  }

  //is called every frame by stage (calls method update)
  override def act(delta: Float): Unit = {
    super.act(ticker.delta / 1000f)
    this.update()
  }

}
