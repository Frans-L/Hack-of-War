package game.main.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.{Batch, BitmapFont, GlyphLayout, Sprite}
import com.badlogic.gdx.scenes.scene2d.Actor
import game.loader.GameTextures
import game.main.players.Player

class ManaBar(owner: Player) extends Actor {

  private val font: BitmapFont = GameTextures.defaultUI.Fonts.normal
  private val textLayout: GlyphLayout = new GlyphLayout()
  private val fontColor: Color = Color.valueOf("#4699a8")

  private val textHeight: Float = 20
  private var text: String = ""

  private val barMargin: Float = 2
  private val bar: Sprite = GameTextures.defaultUI.atlas.createSprite(GameTextures.UI.manaBar)
  private val fill: Sprite = GameTextures.defaultUI.atlas.createSprite(GameTextures.UI.manaBarFill)
  private val fillColor: Color = Color.valueOf("#4699a8")

  //initializes sprites and this actor
  fill.setColor(fillColor)
  fill.setAlpha(0.40f)
  this.setSize(280, 30)
  this.setOrigin(0, 0)
  this.setPosition(0, 0)

  /** Updates all sprites. */
  def updateSprite(): Unit = {
    bar.setOrigin(this.getOriginX, this.getOriginY)
    bar.setSize(this.getWidth, this.getHeight)
    bar.setScale(this.getScaleX, this.getScaleY)
    bar.setPosition(this.getX, this.getY)

    fill.setOrigin(this.getOriginX, this.getOriginY)
    fill.setSize((owner.mana / Player.maxMana) * (this.getWidth - barMargin * 2),
      this.getHeight - barMargin * 2)
    fill.setScale(this.getScaleX, this.getScaleY)
    fill.setPosition(this.getX + barMargin, this.getY + barMargin)
  }

  /** Is called every frame by stage (calls method update) */
  override def act(delta: Float): Unit = {
    super.act(delta)

    fill.setSize((owner.mana / Player.maxMana) * (this.getWidth - barMargin * 2),
      this.getHeight - barMargin * 2)
    text = owner.mana.toInt.toString
  }


  override def draw(batch: Batch, a: Float): Unit = {
    bar.draw(batch)
    fill.draw(batch)

    //draws the text
    if (this.getScaleX > 0 && this.getScaleY > 0) {
      font.getData.setScale(this.getScaleX, this.getScaleY)
      font.setColor(fontColor)
      textLayout.setText(font, text)
      val posX = this.getX + this.getWidth * this.getScaleX / 2f - textLayout.width / 2
      val posY = this.getY + this.getHeight + textLayout.height + textHeight
      font.draw(batch, textLayout, posX, posY)
    }

  }

  /** Updates also the sprite. */
  override def setPosition(x: Float, y: Float): Unit = {
    super.setPosition(x, y)
    updateSprite()
  }


}
