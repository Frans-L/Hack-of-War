package game.main.gameworld.gameobject

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

/** */
trait ObjectElement {

  def update(parent: GameObject, delta: Int): Unit = Unit

  def draw(parent: GameObject, shapeRender: ShapeRenderer): Unit = Unit

  def draw(parent: GameObject, batch: Batch): Unit = Unit

  /** Throws an error if the parent is not valid! */
  def checkParent(parent: GameObject): Unit

}
