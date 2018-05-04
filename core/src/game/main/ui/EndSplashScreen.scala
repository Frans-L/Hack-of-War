package game.main.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.{Batch, BitmapFont, GlyphLayout, Sprite}
import com.badlogic.gdx.scenes.scene2d.{Actor, InputEvent, InputListener, Touchable}
import game.loader.GameTextures


/** Temporary splash screen for testing purposes... */
class EndSplashScreen(resultText: String, width: Float, height: Float, endAction: () => Unit) extends Actor {

  this.setBounds(-width / 2, -height / 2, width, height)

  private val font: BitmapFont = GameTextures.defaultUI.Fonts.big
  private val textLayout: GlyphLayout = new GlyphLayout(font, resultText)
  private val fontColor: Color = Color.valueOf("#4699a8")

  val background: Sprite = GameTextures.defaultUI.atlas.createSprite(GameTextures.UI.endBackground)
  background.setSize(this.getWidth, this.getHeight)
  background.setPosition(this.getX, this.getY)
  background.setColor(Color.BLACK)
  background.setAlpha(0.66f)

  this.setTouchable(Touchable.enabled)

  addListener(new InputListener() {

    override def touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean = true

    override def touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Unit = {
      endAction()
    }
  })

  override def draw(batch: Batch, a: Float): Unit = {
    background.draw(batch)

    font.getData.setScale(this.getScaleX, this.getScaleY)
    font.setColor(fontColor)
    textLayout.setText(font, resultText)
    val posX = -textLayout.width / 2
    val posY = 75
    font.draw(batch, textLayout, posX, posY)

  }


}
