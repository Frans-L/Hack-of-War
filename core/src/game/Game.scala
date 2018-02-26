package game

import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/**
  * Created by Frans on 26/02/2018.
  */
class Game(world: World) extends GameElement {

  val a: ActiveObject = new ActiveObject(0.0, 0.0)

  override def draw(shapeRender: ShapeRenderer): Unit = {

    shapeRender.setColor(1, 0, 1, 1)
    shapeRender.circle(0, 0, 25)
    shapeRender.rect(world.left, world.maxDown, world.width - 1, world.maxHeight - 1)
    shapeRender.rect(world.maxLeft, world.down, world.maxWidth - 1, world.height - 1)

    a.draw(shapeRender)
  }

  override def update(ticker: Ticker): Unit = {
    a.update(ticker)
  }

}
