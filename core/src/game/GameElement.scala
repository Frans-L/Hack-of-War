package game

import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
  * Created by Frans on 26/02/2018.
  */
trait GameElement {

  def update(ticker: Ticker): Unit

  def draw(shapeRender: ShapeRenderer): Unit

}
