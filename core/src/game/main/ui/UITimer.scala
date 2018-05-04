package game.main.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.{Batch, BitmapFont, GlyphLayout}
import com.badlogic.gdx.scenes.scene2d.Actor
import game.loader.GameTextures
import game.util.TimerSecond

class UITimer(timer: TimerSecond) extends Actor {

  private val font: BitmapFont = GameTextures.defaultUI.Fonts.normal
  private val textLayout: GlyphLayout = new GlyphLayout()
  private val fontColor: Color = Color.valueOf("#4699a8")

  override def draw(batch: Batch, a: Float): Unit = {
    if (this.getScaleX > 0 && this.getScaleY > 0) {
      font.getData.setScale(this.getScaleX, this.getScaleY)
      font.setColor(fontColor)
      textLayout.setText(font, timer.toString)
      val posX = this.getX - textLayout.width / 2
      val posY = this.getY + textLayout.height
      font.draw(batch, textLayout, posX, posY)
    }
  }

}
