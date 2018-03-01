package game.ui

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.Ticker
import game.loader.GameTextures
import game.objects.ActiveObject
import game.objects.Soldier.texture

/**
  * Created by Frans on 01/03/2018.
  */
object SoldierCard {

  val texture = GameTextures.Units.card1

  def create(gameTextures: GameTextures, shapeRenderer: ShapeRenderer): UICard = {
    val obj: UICard = new UICard(gameTextures.atlas.createSprite(texture), shapeRenderer)
    obj
  }

}
