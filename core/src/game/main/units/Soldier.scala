package game.main.units

import game.GameElement
import game.loader.GameTextures
import game.main.physics.PhysicsWorld
import game.main.physics.collision.PolygonBody
import game.main.objects.UnitObject
import game.main.players.Player
import game.util.{Ticker, Utils, Vector2e}

/**
  * Created by Frans on 01/03/2018.
  */
object Soldier {

  val texture: Seq[String] = GameTextures.Units.unit1


  def create(owner: Player, physWorld: PhysicsWorld,
             x: Float, y: Float): UnitObject = {


    //size info
    val scale = 1.5f //tmp scale
    val w = 100f / scale
    val h = 75f / scale

    val body: PolygonBody = PolygonBody.triangleCollBody(0, h, w, h / 2f)

    val obj: UnitObject = new UnitObject(
      GameTextures.defaultTextures.atlas.createSprite(texture(owner.colorIndex)),
      owner, physWorld, body,
      Vector2e(x, y), Vector2e(w, h))

    obj
  }


}
