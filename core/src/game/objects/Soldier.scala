package game.objects

import game.Ticker
import game.loader.GameTextures

/**
  * Created by Frans on 01/03/2018.
  */
object Soldier {

  val texture: String = GameTextures.Units.unit1


  def create(ticker: Ticker, gameTextures: GameTextures): ActiveObject = {
    val obj: ActiveObject = new ActiveObject(ticker, gameTextures.atlas.createSprite(texture))
    obj.setSize(100 / 2f, 75 / 2)
    obj
  }


}
