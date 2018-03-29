package game.main.physics.objects.units

import game.GameElement
import game.loader.GameTextures
import game.main.Player
import game.main.physics.{CollisionBody, PhysicsWorld}
import game.main.physics.objects.UnitObject
import game.util.{Ticker, Utils, Vector2e}

/**
  * Created by Frans on 01/03/2018.
  */
object Soldier {

  val texture: Seq[String] = GameTextures.Units.unit1


  def create(textures: GameTextures,
             owner: Player, collHandler: PhysicsWorld,
             x: Float, y: Float, colorIndex: Int): UnitObject = {


    val scale = 1.5f
    val w = 99f / scale
    val h = 75f / scale

    val body: CollisionBody = Utils.triangleCollBody(0, h, w, h / 2f)

    val obj: UnitObject = new UnitObject(
      textures.atlas.createSprite(texture(colorIndex)),
      owner, collHandler, body,
      Vector2e(x, y), Vector2e(w, h))

    obj
  }


}
