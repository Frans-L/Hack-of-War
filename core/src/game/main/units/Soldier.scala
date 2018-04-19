package game.main.units

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import game.GameElement
import game.loader.GameTextures
import game.main.gameMap.{IconPath, Path}
import game.main.physics.PhysicsWorld
import game.main.physics.collision.PolygonBody
import game.main.objects.{UnitObject, UnitPath}
import game.main.players.Player
import game.util.{Ticker, Utils, Vector2e}

/**
  * Created by Frans on 01/03/2018.
  */
object Soldier {

  val texture: Seq[String] = GameTextures.Units.unit1


  //size info
  private val scale = 1.5f
  val width: Float = 100f / scale
  val height: Float = 75f / scale

  def create(owner: Player, physWorld: PhysicsWorld,
             x: Float, y: Float, path: Path,
             random: Boolean = false): UnitObject = {

    val sprite = GameTextures.defaultTextures.atlas.createSprite(texture(owner.colorIndex))
    sprite.setSize(width, height)

    val icon = pathIcon(owner)

    val body: PolygonBody = PolygonBody.triangleCollBody(width, height / 2f, 0, height)

    val unitPath = {
      if (!random) new UnitPath(path, icon, path.findOffset(x, y), body.getRadiusScaled * 2)
      else new UnitPath(path, icon, path.randomOffset, body.getRadiusScaled * 2)
    }


    val obj: UnitObject = new UnitObject(sprite, owner, physWorld, body, Some(unitPath))

    obj.pos.set(unitPath.path.head)
    obj.updateShape() //important to remember after force changing pos

    obj
  }

  /** Returns the icon of the soldier */
  def pathIcon(owner: Player): Sprite = {
    val darkness = 0.25f
    val sprite = GameTextures.defaultTextures.atlas.createSprite(texture(owner.colorIndex))
    sprite.setColor(darkness, darkness, darkness, 0.15f)
    sprite.setSize(width, height)
    sprite
  }


}
