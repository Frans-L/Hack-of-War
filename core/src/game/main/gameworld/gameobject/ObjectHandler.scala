package game.main.gameworld.gameobject

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.GameElement
import game.main.gameworld.collision.CollisionHandler
import game.main.gameworld.gamemap.Map
import game.main.gameworld.gameobject.ObjectHandler.Level
import game.main.gameworld.gameobject.objects.PhysicsObject
import game.util.Dimensions

import scala.collection.mutable


object ObjectHandler {

  //3 different render && update layers
  object Level extends Enumeration {
    type Level = Value
    val map: Value = Value(0)
    val ground: Value = Value(1)
    val top: Value = Value(2
    )
    val size: Int = 3
  }

}

/** Handles all the objects, collisions and the map. */
class ObjectHandler(dimensions: Dimensions) extends GameElement {

  val drawObjects: Array[mutable.Buffer[GameObject]] = Array.fill(Level.size)(mutable.Buffer.empty)
  val updateObjects: Array[mutable.Buffer[GameObject]] = Array.fill(Level.size)(mutable.Buffer.empty)

  val collHandler: CollisionHandler = new CollisionHandler(dimensions)
  val map: Map = new Map(this, this, dimensions) //creates new map
  collHandler.mapFilter += map.owner //adds default filter to collHandler

  //adds map to be drawn, map is static => no need to update
  this.addObject(map.mapObject, Level.map, update = false)

  /** Updates all objects */
  override def update(): Unit = {
    collHandler.update() //updates the collisions
    updateObjects.foreach(_.foreach(_.update())) //updates every objects

    clearDeletedUnits()
    collHandler.clearDeletedUnits()
  }

  /** Draws all */
  override def draw(batch: Batch): Unit = {
    drawObjects.foreach(_.foreach(_.draw(batch)))
  }

  /** Draws all */
  override def draw(shapeRender: ShapeRenderer): Unit = {
    drawObjects.foreach(_.foreach(_.draw(shapeRender)))
  }


  /** Removes deleted units */
  private def clearDeletedUnits(): Unit = {
    for (i <- 0 until Level.size) {
      deleteUnits(drawObjects(i))
      deleteUnits(updateObjects(i))
    }

    def deleteUnits(buffer: mutable.Buffer[GameObject]): Unit = {
      for (i <- buffer.indices.reverse) if (buffer(i).canBeDeleted) buffer.remove(i)
    }
  }

  /** Adds unit to be updated. */
  def addObject(obj: GameObject, level: Level.Value = Level.ground,
                update: Boolean = true, draw: Boolean = true,
                collision: Boolean = true, owner: GameElement = this): Unit = {
    if (update) updateObjects(level.id) += obj
    if (draw) drawObjects(level.id) += obj
    if (collision && obj.isInstanceOf[objects.PhysicsObject]) {
      if (owner == this) Gdx.app.log(this.toString, obj.toString + " is without owner!")
      addToCollHandler(owner, obj.asInstanceOf[objects.PhysicsObject])
    }

  }

  /** Adds a physicsObject to collHandler */
  def addToCollHandler(owner: GameElement, obj: PhysicsObject): Unit = {
    collHandler.addUnit(owner, obj)
  }

}
