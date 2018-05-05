package game.main.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.{Batch, BitmapFont, GlyphLayout, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.{Interpolation, Vector2, Vector3}
import com.badlogic.gdx.scenes.scene2d.actions.{Actions, MoveToAction, ParallelAction, ScaleByAction}
import com.badlogic.gdx.scenes.scene2d._
import game.loader.GameTextures
import game.main.gameworld.gameobject.GameObject
import game.util.Ticker


object UICard {

  val easUse: Interpolation = Interpolation.pow2In
  val easUseDur: Long = 500 //ms

  object State extends Enumeration {
    val IDLE, RETURN, MOVE, DRAG, ACTION = Value
  }


}

/**
  * Created by Frans on 28/02/2018.
  */
class UICard(cardSprite: Sprite, icon: GameObject, text: String) extends Actor {

  private val font: BitmapFont = GameTextures.defaultUI.Fonts.normal
  private val textLayout: GlyphLayout = new GlyphLayout(font, text)
  private val fontColor: Color = Color.valueOf("#4699a8")

  private var startPos: Vector2 = new Vector2(0, 0) //tells the spot to stay when not moved
  var state: UICard.State.Value = UICard.State.IDLE
  private var moved = false

  private var dragPos: Vector2 = new Vector2(0, 0) //the pos which were touched

  this.setTouchable(Touchable.enabled)

  def updateSprites(): Unit = {

    cardSprite.setOrigin(0, 0)
    cardSprite.setSize(this.getWidth, this.getHeight)
    cardSprite.setScale(this.getScaleX, this.getScaleY)
    cardSprite.setPosition(this.getX, this.getY)

    icon.scale.set(this.getScaleX, this.getScaleY)
    icon.pos.set(this.centerX, this.centerY)

  }

  def update(): Unit = {
    updateSprites()
    icon.update()
  }

  override def draw(batch: Batch, fl: Float): Unit = {

    cardSprite.draw(batch)
    icon.draw(batch)

    if (this.getScaleX > 0 && this.getScaleY > 0) {
      font.getData.setScale(this.getScaleX, this.getScaleY)
      font.setColor(fontColor)
      textLayout.setText(font, text)
      val posX = this.getX + this.getWidth * this.getScaleX / 2f - textLayout.width / 2
      val posY = this.getY + textLayout.height * 2
      font.draw(batch, textLayout, posX, posY)
    }

  }


  /** Animation when the card is used */
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

  /** Called when drag starts */
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

  /** Updates sprite pos */
  def drag(x: Float, y: Float): (Float, Float) = {
    this.setPosition(
      this.getX + (x - dragPos.x) * getScaleX,
      this.getY + (y - dragPos.y) * getScaleY)
    (this.getX + dragPos.x * getScaleX, this.getY + dragPos.y * getScaleY)
  }

  /** Stops the drag and returns final pos */
  def stopDrag(x: Float, y: Float): (Float, Float) = {

    moved = true //card is moved
    state = UICard.State.IDLE
    this.setPosition(
      this.getX + (x - dragPos.x) * getScaleX,
      this.getY + (y - dragPos.y) * getScaleY)

    (this.getX + dragPos.x * getScaleX, this.getY + dragPos.y * getScaleY)
  }


  /** Card returns to the original spot with animation */
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

  /** Card moves to it original spot with fast animation */
  private def updateStartPos(): Unit = {
    val animTime = 200 / 1000f

    val anim = Actions.parallel(
      Actions.moveTo(startPos.x, startPos.y, animTime, Interpolation.circle),
      Actions.scaleTo(1f, 1f, animTime, Interpolation.circle)
    )
    this.addAction(anim)
  }


  /** Sets the new start pos, and move card to it */
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

  /** Is called every frame by stage (calls method update) */
  override def act(delta: Float): Unit = {
    super.act(delta)
    this.update()
  }

  /** Returns the center position of the actor */
  private def centerX: Float = this.getX + this.getWidth * this.getScaleX / 2f

  /** Returns the center position of the actor */
  private def centerY: Float = this.getY + this.getHeight * this.getScaleY / 2f

}
