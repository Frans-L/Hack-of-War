package game.objects

import com.badlogic.gdx.math.Polygon
import game.Ticker
import game.loader.GameTextures
import game.main.{CollisionBody, CollisionHandler}
import game.util.{Utils, Vector2e}

/**
  * Created by Frans on 01/03/2018.
  */
object Soldier {

  val texture: String = GameTextures.Units.unit1


  def create(ticker: Ticker, textures: GameTextures, collDetect: CollisionHandler,
             x: Float, y: Float): UnitObject = {


    val scale = 1.5f
    val w = 99f / scale
    val h = 75f / scale

    val body: CollisionBody = Utils.triangleCollBody(0, h, w, h / 2f)

    val obj: UnitObject = new UnitObject(
      textures.atlas.createSprite(texture), collDetect, body,
      Vector2e(x, y), Vector2e(w, h))

    obj
  }


}
