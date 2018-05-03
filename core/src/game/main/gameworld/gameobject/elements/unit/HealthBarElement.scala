package game.main.gameworld.gameobject.elements.unit

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.math.{Interpolation, Vector2}
import game.loader.GameTextures
import game.main.gameworld.gameobject
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.gameworld.gameobject.{GameObject, objects}
import game.util.{CountdownTimer, Vector2e}

class HealthBarElement(maxHealth: Float, showTime: Int = Int.MaxValue) extends UnitElement {

  val posY: Float = 20 //posY from the highest point of the object
  val opacity = 0.30f

  val fullHealthColor: Color = Color.valueOf("#0e9302") //green
  val zeroHealthColor: Color = Color.valueOf("#700101") //red

  private val scale = 2f
  val size: Vector2 = Vector2e(100 / scale, 20 / scale)
  val margin: Vector2 = Vector2e(2 / scale, 2 / scale)

  private val bar: Sprite = GameTextures.default.atlas.createSprite(GameTextures.Units.healthBar)
  private val fill: Sprite = GameTextures.default.atlas.createSprite(GameTextures.Units.healthBarFill)

  bar.setColor(Color.BLACK)
  bar.setSize(size.x, size.y)
  fill.setSize(size.x - margin.x * 2, size.y - margin.y * 2)

  private val animTimer = new CountdownTimer(250).stop()

  override def update(p: GameObject, delta: Int): Unit = {
    val parent = p.asInstanceOf[objects.UnitObject]

    //sets positions
    val x = parent.pos.x - size.x / 2f
    val y = parent.collBody.highestPointY + posY
    bar.setPosition(x, y)
    fill.setPosition(x + margin.x, y + margin.y)

    //sets fill and color
    val percent = parent.health / maxHealth
    fill.setSize(size.x * percent - margin.x * 2f, size.y - margin.y * 2f)
    fill.setColor(zeroHealthColor)
    fill.setColor(fill.getColor.lerp(fullHealthColor,
      Interpolation.fade.apply(percent)))

    //sets visibility with animation
    if (parent.lastHitTick != UnitObject.noHitTick && parent.ticker.total < parent.lastHitTick + showTime)
      animTimer.forward().start()
    else
      animTimer.backward().start()

    val alpha: Float = opacity * Interpolation.fade.apply(animTimer.progress)
    fill.setAlpha(alpha)
    bar.setAlpha(alpha)

    animTimer.update(delta) //update anim timer
  }

  override def draw(parent: GameObject, batch: Batch): Unit = {
    fill.draw(batch)
    bar.draw(batch)
  }

}
