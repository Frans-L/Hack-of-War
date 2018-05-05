package game.main.gameworld.gameobject.objects.elements.unit

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Interpolation
import game.main.gameworld.gameobject.{GameObject, objects}
import game.main.gameworld.gameobject.objects.elements.TextureElement
import game.main.gameworld.gameobject.objects.UnitObject
import game.util.CountdownTimer

class UnitTextureElement(texture: TextureRegion, brightness: Float, hitAnimTime: Int = 250)
  extends TextureElement(texture, brightness) with UnitElement {

  val normalColor: Color = color.cpy()
  val hitColor: Color = color.cpy().add(1, 1, 1, 0f)

  private val animTimer = new CountdownTimer(hitAnimTime).stop()
  private var lastHitTick: Long = UnitObject.noHitTick

  override def update(p: GameObject, delta: Int): Unit = {
    val parent = p.asInstanceOf[objects.UnitObject]

    if (lastHitTick != parent.lastHitTick) animTimer.backward().reset().start()
    //else if (animTimer.isEnd) animTimer.reset().stop()

    color.set(normalColor)
    color.set(color.lerp(hitColor, Interpolation.pow2Out.apply(animTimer.progress)))
    //parent.opacity = 1 - 0.4f * Interpolation.pow2Out.apply(animTimer.progress)

    lastHitTick = parent.lastHitTick
    animTimer.update(delta)
  }

}
