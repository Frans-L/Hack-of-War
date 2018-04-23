package game.main.objects.improved

import com.badlogic.gdx.{Game, Gdx}
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import game.GameElement

import scala.annotation.tailrec
import scala.collection.mutable

trait ObjectElement {

  val elements: mutable.Buffer[ObjectElement] = mutable.Buffer.empty

  def update(father: GameObject, delta: Int): Unit = {
    elements.foreach(_.update(father, delta))
  }

  def draw(shapeRender: ShapeRenderer): Unit = {
    elements.foreach(_.draw(shapeRender))
  }

  def draw(batch: Batch): Unit = {
    elements.foreach(_.draw(batch))
  }

  /** Adds a new child to a object. */
  def addElement(objectElement: ObjectElement): this.type = {
    elements += objectElement
    this
  }

}
