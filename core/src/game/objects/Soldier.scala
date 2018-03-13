package game.objects

import game.Ticker
import game.loader.GameTextures
import game.main.CollisionDetector

/**
  * Created by Frans on 01/03/2018.
  */
object Soldier {

  val texture: String = GameTextures.Units.unit1


  def create(ticker: Ticker, textures: GameTextures, collDetect: CollisionDetector,
             x: Float, y: Float): ActiveObject = {
    val obj: ActiveObject = new ActiveObject(
      textures.atlas.createSprite(texture), collDetect,
      x, y, 100 / 1.5f, 75 / 1.5f)
    obj
  }


}
