package game

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
  * Created by Frans on 26/02/2018.
  */
trait GameElement {

  var ticker: Ticker = Ticker.defaultTicker //sets the ticker to default

  def update(): Unit //updates the element using the ticker

  def draw(shapeRender: ShapeRenderer): Unit

  def draw(batch: Batch): Unit

}
