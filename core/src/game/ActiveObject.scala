package game

import com.badlogic.gdx.{Application, Gdx}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
  * Created by Frans on 26/02/2018.
  */
class ActiveObject(var posX: Double, var posY: Double) extends GameObject {

  override def update(ticker: Ticker): Unit = {
    if (enabled) {

      posX += 0.01d * ticker.elapsed

      Gdx.app.setLogLevel(Application.LOG_DEBUG)
      Gdx.app.log("tmp", "posX: " + posX)
      Gdx.app.log("tmp", "Ticker: " + ticker.elapsed)

    }
  }

  override def draw(shapeRender: ShapeRenderer): Unit = {
    if (visibile) {
      shapeRender.circle(posX.toInt, posY.toInt, 50)
    }
  }


}
