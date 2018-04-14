package game.main.physics

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.GameElement
import game.loader.GameTextures
import game.main.physics.objects.{BorderSprite, CollisionObject}
import game.util.{Dimensions, Utils, Vector2e}

import scala.collection.mutable


object Map {

  val mapBorder: String = GameTextures.Units.mapBorder
  val mapBorderShort: String = GameTextures.Units.mapBorderShort
  val mapBorderWide: String = GameTextures.Units.mapBorderWide
}

/**
  * Created by Frans on 06/03/2018.
  */
class Map(val dimensions: Dimensions,
          textures: GameTextures,
          physWorld: PhysicsWorld) extends GameElement {

  private val elements: mutable.Buffer[BorderSprite] = mutable.Buffer[BorderSprite]()

  private val collAccuracy = 20 //collisionAccuracy
  private val collMap = Array.ofDim[Boolean](
    dimensions.maxWidth / collAccuracy + 1,
    dimensions.maxHeight / collAccuracy + 1)

  val collObjects: mutable.Buffer[CollisionObject] = mutable.Buffer[CollisionObject]()

  initializeMap()
  createCollisionMap()


  override def update(): Unit = ???

  override def draw(shapeRender: ShapeRenderer): Unit = ???

  override def draw(batch: Batch): Unit = {
    elements.foreach(_.draw(batch))
  }

  /**
    * Returns true if collided with static collision map
    */
  def collide(x: Float, y: Float): Boolean = {
    if (dimensions.isInside(x + 0.5f, y + 0.5f))
      collMap(
        ((-dimensions.maxLeft + x) / collAccuracy + 0.5f).toInt)(
        ((-dimensions.maxDown + y) / collAccuracy + 0.5f).toInt)
    else
      false
  }

  /**
    * Creates a static collision map from 'elements'
    */
  private def createCollisionMap(): Unit = {
    for (x <- collMap.indices) {
      for (y <- collMap.head.indices) {
        collMap(x)(y) = collObjects.exists(
          _.collBody.contains(dimensions.maxLeft + x * collAccuracy, dimensions.maxDown + y * collAccuracy))
      }
    }
  }

  /**
    * Creates a map
    */
  private def initializeMap(): Unit = {

    //Down border
    var height: Float = 200 + 180
    var y: Float = dimensions.maxDown
    var x: Float = dimensions.maxLeft
    collObjects += new CollisionObject(this, physWorld, Utils.rectangleCollBody(x, y, dimensions.maxWidth, height))

    for (i <- 0 to 8) { //to create collisionMap
      elements += new BorderSprite(
        Vector2e(x, y),
        Vector2e(345 - ((i % 2) * 165), height),
        Vector2e(1, 0),
        textures.atlas.createSprite(Map.mapBorder))
      x = elements.last.nextToX
    }


    //Up border
    height = 50 + 180
    y = dimensions.maxUp - height
    x = dimensions.maxLeft
    collObjects += new CollisionObject(this, physWorld, Utils.rectangleCollBody(x, y, dimensions.maxWidth, height))

    for (i <- 0 to 8) { //to create collisionMap
      elements += new BorderSprite(
        Vector2e(x, y),
        Vector2e(345 - ((i % 2) * 165), height),
        Vector2e(1, 0),
        textures.atlas.createSprite(Map.mapBorder))

      x = elements.last.nextToX
    }


    //middle
    y = 0
    x = dimensions.left + 345 + 180
    var width = 435
    height = 75
    collObjects += new CollisionObject(this, physWorld, Utils.rectangleCollBody(x, y, width * 2, height))

    for (i <- 0 until 2) { //to create collisionMap
      elements += new BorderSprite(
        Vector2e(x, y),
        Vector2e(width, height),
        Vector2e(1, 0),
        textures.atlas.createSprite(Map.mapBorder))

      x = elements.last.nextToX
    }

  }
}
