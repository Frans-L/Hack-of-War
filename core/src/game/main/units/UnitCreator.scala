package game.main.units

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.utils.{Pool, Pools}
import game.loader.{GameTextures, UnitTextures}
import game.main.gameMap.{IconPath, Path}
import game.main.objects.brains.FollowPath
import game.main.objects.{UnitObject, UnitPath}
import game.main.physics.PhysicsWorld
import game.main.physics.collision.PolygonBody
import game.main.players.Player
import game.util.Vector2e

trait UnitCreator {

  val cost: Int

  val texture: UnitTextures
  val width: Float
  val height: Float

  /** Sets the all stats to to unit */
  protected def setStats(obj: UnitObject, path: Path): Unit

  /** Creates a new unit */
  def create(owner: Player, physWorld: PhysicsWorld,
             x: Float, y: Float, path: Path,
             random: Boolean = false): UnitObject = {

    val icon = pathIcon(owner)
    val body: PolygonBody = PolygonBody.triangleCollBody(width, height / 2f, 0, height)

    //creates the unit
    val size = Vector2e(width, height)
    val obj = UnitObject.pool.obtain().init(texture, size, owner, physWorld, body)

    //find the route
    val p: Path = path.copy
    if(random) p.setOffset(p.randomOffset)
    obj.pos.set(p.head)
    obj.updateShape() //important to remember after force changing pos

    setStats(obj, p) //sets the specific unit stats

    obj
  }

  /** Returns the icon of the unit */
  def pathIcon(owner: Player): Sprite = {
    val darkness = 0.25f
    val sprite = GameTextures.defaultTextures.atlas.createSprite(texture.main(owner.colorIndex))
    sprite.setColor(darkness, darkness, darkness, 0.15f)
    sprite.setSize(width, height)
    sprite
  }

  /** Returns the card icon of the unit */
  def cardIcon(owner: Player): Sprite = {
    val darkness = 0f
    val sprite = GameTextures.defaultTextures.atlas.createSprite(texture.main(owner.colorIndex))
    //sprite.setColor(darkness, darkness, darkness, 0.15f)
    sprite.setSize(width, height)
    sprite
  }

}
