package game.main.objects.improved

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.GameElement
import game.main.objects.improved.ObjectHandler.Level

import scala.collection.mutable


object ObjectHandler {

  object Level extends Enumeration {
    type Level = Value

    val under: Value = Value(0)
    val map: Value = Value(1)
    val ground: Value = Value(2)
    val high: Value = Value(3)
    val top: Value = Value(4)

    val size: Int = 5
  }

}

class ObjectHandler extends GameElement {

  val drawObjects: Array[mutable.Buffer[GameObject]] = Array.fill(Level.size)(mutable.Buffer.empty)
  val updateObjects: Array[mutable.Buffer[GameObject]] = Array.fill(Level.size)(mutable.Buffer.empty)

  override def update(): Unit = {
    updateObjects.foreach(
      _.foreach(_.update())
    )
  }

  override def draw(shapeRender: ShapeRenderer): Unit = {
    drawObjects.foreach(
      _.foreach(_.draw(shapeRender))
    )
  }

  override def draw(batch: Batch): Unit = {
    drawObjects.foreach(
      _.foreach(_.draw(batch))
    )
  }

  def addObject(obj: GameObject, level: Level.Value,
                update: Boolean = true, draw: Boolean = true): Unit = {
    if (update) updateObjects(level.id) += obj
    if (draw) drawObjects(level.id) += obj
  }
}
