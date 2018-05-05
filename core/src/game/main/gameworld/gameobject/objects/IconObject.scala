package game.main.gameworld.gameobject.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.{Batch, TextureRegion}
import com.badlogic.gdx.math.Interpolation
import game.main.gameworld.gameobject.GameObject
import game.main.gameworld.gameobject.elements.RelativeTextureElement
import game.main.players.Player
import game.util.CountdownTimer

/** Updates the color whether player has enough mana or not.*/
class IconObject(var owner: Player, var cost: Int) extends GameObject {

  val activeColor: Color = new Color(1f, 1f, 1f, 1f)
  private val brightness: Float = 1f
  val inactiveColor: Color = new Color(brightness, brightness, brightness, 0.3f)

  val color: Color = new Color(1f, 1f, 1f, 1f)
  val animTimer: CountdownTimer = new CountdownTimer(400).stop()

  override def update(): Unit = {
    if (owner.mana >= cost) animTimer.forward().start()
    else animTimer.backward().start()
    color.set(inactiveColor)
    color.lerp(activeColor, Interpolation.fade(animTimer.progress))

    animTimer.update(ticker.delta)
    super.update()
  }

}
