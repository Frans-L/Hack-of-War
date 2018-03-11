package game.objects

import game.Ticker
import game.loader.GameTextures

/**
  * Created by Frans on 01/03/2018.
  */
object Soldier {

  val texture: String = GameTextures.Units.unit1


  def create(ticker: Ticker, textures: GameTextures, x: Float, y: Float): ActiveObject = {
    val obj: ActiveObject = new ActiveObject(ticker, textures.atlas.createSprite(texture), x, y)
    obj.setSize(100 / 1.5f, 75 / 1.5f)
    obj
  }


}
