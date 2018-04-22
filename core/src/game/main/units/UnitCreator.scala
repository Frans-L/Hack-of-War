package game.main.units

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import game.loader.GameTextures
import game.main.gameMap.Path
import game.main.objects.{UnitObject, UnitPath}
import game.main.physics.PhysicsWorld
import game.main.physics.collision.PolygonBody
import game.main.players.Player
import game.main.units.Soldier.{height, texture, width}

trait UnitCreator {

  val cost: Int

  val texture: Seq[String]
  val width: Float
  val height: Float

  /** Sets the all stats to to unit */
  protected def setStats(obj: UnitObject): Unit

  /** Creates a new unit */
  def create(owner: Player, physWorld: PhysicsWorld,
             x: Float, y: Float, path: Path,
             random: Boolean = false): UnitObject = {

    val sprite = GameTextures.defaultTextures.atlas.createSprite(texture(owner.colorIndex))
    sprite.setSize(width, height)
    val icon = pathIcon(owner)
    val body: PolygonBody = PolygonBody.triangleCollBody(width, height / 2f, 0, height)

    //moving path
    val unitPath = {
      if (!random) new UnitPath(path, icon, path.findOffset(x, y), body.getRadiusScaled * 2)
      else new UnitPath(path, icon, path.randomOffset, body.getRadiusScaled * 2)
    }

    //creates the unit
    val obj: UnitObject = new UnitObject(sprite, owner, physWorld, body, Some(unitPath))
    obj.pos.set(unitPath.path.head)
    obj.updateShape() //important to remember after force changing pos

    setStats(obj)
    obj
  }

  /** Returns the icon of the unit */
  def pathIcon(owner: Player): Sprite = {
    val darkness = 0.25f
    val sprite = GameTextures.defaultTextures.atlas.createSprite(texture(owner.colorIndex))
    sprite.setColor(darkness, darkness, darkness, 0.15f)
    sprite.setSize(width, height)
    sprite
  }

  /** Returns the card icon of the unit */
  def cardIcon(owner: Player): Sprite = {
    val darkness = 0f
    val sprite = GameTextures.defaultTextures.atlas.createSprite(texture(owner.colorIndex))
    //sprite.setColor(darkness, darkness, darkness, 0.15f)
    sprite.setSize(width, height)
    sprite
  }

}
