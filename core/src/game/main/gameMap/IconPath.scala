package game.main.gameMap

import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Interpolation
import game.GameElement
import game.main.objects.UnitPath
import game.util.CountdownTimer

/** Holds a path, and an icon. */
class IconPath(private var pathOrg: Path, val icon: Sprite) extends GameElement {

  //path info
  val path: Path = pathOrg.copy
  private var offset: Float = 0

  //how to draw icons info
  val drawTimer: CountdownTimer = new CountdownTimer(500)
  var movingSpeed = 0.15f
  var startPos = 0f

  private val orginalAlpha = icon.getColor.a
  var iconGap: Float = 6 * icon.getWidth * icon.getScaleX


  /** Sets the offset. */
  def setOffset(off: Float): Unit = {
    path.changeRoute(pathOrg.copy.getRoute)
    path.setOffset(off)
    offset = off
  }

  /** Changes the path. If the path is same, doesn't do anything. */
  def changePath(p: Path): Unit = {
    if (p != pathOrg) {
      path.changeRoute(p.getRoute)
      path.maxOffset = p.maxOffset
      offset = 0
      pathOrg = p
    }

  }

  /** Returns the copy of the iconPaths */
  def copy(): IconPath = {
    val cpy = new IconPath(pathOrg, icon)
    cpy.setOffset(offset)
    cpy
  }

  def getOrgPath: Path = pathOrg

  /** Draws the path if the time is still running */
  override def draw(batch: Batch): Unit = {
    if (drawTimer.isOnReversed) {
      icon.setAlpha(orginalAlpha * Interpolation.fade(drawTimer.progress))
      path.draw(batch, icon, iconGap, startPos)
      icon.setAlpha(orginalAlpha)
    }
  }

  override def update(): Unit = {
    drawTimer.update(ticker.delta)
    startPos = (startPos + movingSpeed * ticker.delta) % iconGap
  }

  override def draw(shapeRender: ShapeRenderer): Unit = ???

}
