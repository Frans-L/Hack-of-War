package game.main.objects.brains

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.GameElement
import game.main.objects.UnitObject

/**
  * Controls the unitobject.
  * Many brains can be attached to one unit.
  */
trait Brain {

  def update(obj: UnitObject)

  def draw(obj: UnitObject, shapeRenderer: ShapeRenderer): Unit = Unit

  def draw(obj: UnitObject, batch: Batch): Unit = Unit

}
