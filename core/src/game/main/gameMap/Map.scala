package game.main.gameMap

import java.nio.file.Path

import com.badlogic.gdx.graphics.g2d.{Batch, Sprite}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import game.GameElement
import game.loader.GameTextures
import game.main.objects.{BorderSprite, CollisionObject}
import game.main.physics.PhysicsWorld
import game.main.physics.collision.{CollisionBody, PolygonBody}
import game.util.{Dimensions, Vector2e}

import scala.collection.mutable


object Map {

  val border: String = GameTextures.Units.mapBorder

  val corner: String = GameTextures.Units.mapCorner
  val trenchCorner: String = GameTextures.Units.mapTrenchCorner
  val middleCorner: String = GameTextures.Units.mapMiddleCorner

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
  var path: Seq[Path] = _ //will be set at initializeMap

  initializeMap()
  createCollisionMap()


  override def update(): Unit = ???

  override def draw(shapeRender: ShapeRenderer): Unit = Unit

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
    * Creates a map, hardcoded
    */
  private def initializeMap(): Unit = {


    //block info
    val borderWidth: Float = 180
    val cornerWidth: Float = 90
    val baseWidth: Float = 310
    val trenchCornerWidth: Float = 230
    val trenchWidth: Float = 660

    val blockWidth = Seq(borderWidth + cornerWidth, baseWidth, trenchCornerWidth,
      trenchWidth, trenchCornerWidth, baseWidth, borderWidth + cornerWidth)

    require(blockWidth.sum.toInt == dimensions.maxWidth) //has to match with screen coords

    val borderHeight: Float = 180
    val upHeight: Float = 50
    val cornerHeight: Float = 90
    val straightHeight: Float = 210 //part between trench max and middle max
    val middleHeight: Float = 70
    val middleMaxHeight: Float = middleHeight + cornerHeight * 2
    val downHeight: Float = 180

    val blockHeight = Seq(borderHeight + downHeight, cornerHeight, straightHeight,
      middleMaxHeight, straightHeight, cornerHeight, upHeight + borderHeight)

    require(blockHeight.sum.toInt == dimensions.maxHeight) //has to match with screen coords

    addBasicElements()
    addCorners()
    addTrenches()
    addMiddle()
    addPath()

    //adds the path
    def addPath(): Unit = {

      val route = Seq(
        Vector2e(blockPosXMiddle(1), blockPosYMiddle(3)),
        Vector2e(blockPosX(2), blockPosYMiddle(1) + blockHeight(2) / 2),
        Vector2e(blockPosX(3), blockPosYMiddle(1) + blockHeight(2) / 2 + blockHeight(1)),
        Vector2e(blockPosX(4), blockPosYMiddle(1) + blockHeight(2) / 2 + blockHeight(1)),
        Vector2e(blockPosX(5), blockPosYMiddle(1) + blockHeight(2) / 2)
      )

      path = Seq(new Path(route, 10, 10))
    }


    //adds basic stuff like borders
    def addBasicElements(): Unit = {

      //basic elements
      val sprite = textures.atlas.createSprite(Map.border)

      var y: Float = dimensions.maxDown
      addHorizontalBlocks(y, borderHeight + downHeight) //down border

      y = dimensions.maxUp - (borderHeight + upHeight)
      addHorizontalBlocks(y, borderHeight + upHeight) //up border

      addVerticalBlocks(dimensions.maxLeft, borderWidth) //left border
      addVerticalBlocks(dimensions.maxRight - borderWidth, borderWidth) //right border


      //a bit prettier way to add all verticals blocks
      def addVerticalBlocks(x: Float, width: Float): Unit = {
        var y: Float = dimensions.maxDown
        addBlock(PolygonBody.rectangleCollBody(x, y, width, dimensions.maxHeight))
        for (i <- blockHeight.indices) {
          addGraphic(x, y, width, blockHeight(i), sprite, 1, 1)
          y = elements.last.nextToY
        }
      }

      //a bit prettier way to add all horizontal blocks
      def addHorizontalBlocks(y: Float, height: Float): Unit = {
        var x: Float = dimensions.maxLeft
        addBlock(PolygonBody.rectangleCollBody(x, y, dimensions.maxWidth, height))
        for (i <- blockWidth.indices) {
          addGraphic(x, y, blockWidth(i), height, sprite, 1, 1)
          x = elements.last.nextToX
        }
      }

    }

    //adds corners
    def addCorners(): Unit = {

      val width: Float = cornerHeight
      val height: Float = cornerWidth
      val sprite = textures.atlas.createSprite(Map.corner)

      //left up corner
      var x: Float = dimensions.maxLeft + blockWidth.head - width - 2
      var y: Float = dimensions.maxUp - (borderHeight + upHeight) - height
      addTriangleElement(x, y, width, height, sprite, flipX = false, flipY = true)

      //right up corner
      x = dimensions.maxRight - blockWidth.head + 2
      addTriangleElement(x, y, width, height, sprite, flipX = true, flipY = true)

      //left down corner
      x = dimensions.maxLeft + blockWidth.head - width - 2
      y = dimensions.maxDown + (borderHeight + downHeight)
      addTriangleElement(x, y, width, height, sprite, flipX = false, flipY = false)

      //right down corner
      x = dimensions.maxRight - blockWidth.head + 2
      addTriangleElement(x, y, width, height, sprite, flipX = true, flipY = false)
    }

    //adds trenches
    def addTrenches(): Unit = {

      var sprite = textures.atlas.createSprite(Map.trenchCorner)
      var height: Float = cornerHeight
      var width: Float = trenchCornerWidth

      //left up
      var x: Float = dimensions.maxLeft + blockWidth.take(2).sum
      var y: Float = dimensions.maxUp - (borderHeight + upHeight) - height
      addTriangleElement(x, y, width, height, sprite, flipX = true, flipY = true)

      //right up
      x = dimensions.maxRight - blockWidth.takeRight(3).sum
      addTriangleElement(x, y, width, height, sprite, flipX = false, flipY = true)

      //left down
      x = dimensions.maxLeft + blockWidth.take(2).sum
      y = dimensions.maxDown + (borderHeight + downHeight)
      addTriangleElement(x, y, width, height, sprite, flipX = true, flipY = false)

      //right down
      x = dimensions.maxRight - blockWidth.takeRight(3).sum
      addTriangleElement(x, y, width, height, sprite, flipX = false, flipY = false)

      //middle part
      sprite = textures.atlas.createSprite(Map.border)

      height = cornerHeight
      width = trenchWidth
      x = dimensions.maxLeft + blockWidth.take(3).sum

      //up
      y = dimensions.maxUp - (borderHeight + upHeight) - height
      addBlock(PolygonBody.rectangleCollBody(x, y, width, height))
      addGraphic(x, y, width, height, sprite, 1, 1)

      //up
      y = dimensions.maxDown + (borderHeight + downHeight)
      addBlock(PolygonBody.rectangleCollBody(x, y, width, height))
      addGraphic(x, y, width, height, sprite, 1, 1)
    }

    //adds middle part
    def addMiddle(): Unit = {

      //middle
      var sprite = textures.atlas.createSprite(Map.border)

      var height: Float = middleHeight
      var width: Float = trenchWidth / 2
      var y: Float = (downHeight - upHeight) / 2 - height / 2
      var x: Float = dimensions.maxLeft + blockWidth.take(2).sum

      val widthSeq = Seq(trenchCornerWidth, width, width, trenchCornerWidth)

      addBlock(PolygonBody.rectangleCollBody(x + trenchCornerWidth, y, width * 2, height))
      for (i <- 0 until 4) { //to create collisionMap
        addGraphic(x, y, widthSeq(i), height, sprite, 1, 1)
        x = elements.last.nextToX
      }

      //middle corners
      sprite = textures.atlas.createSprite(Map.trenchCorner)
      height = cornerHeight
      width = trenchCornerWidth

      //lef and righ
      val xSeq = Seq(
        dimensions.maxLeft + blockWidth.take(2).sum + 1,
        dimensions.maxRight - blockWidth.take(3).sum - 1)

      var body: PolygonBody = null //tmp variable

      for (i <- 0 until 2) {
        x = xSeq(i)

        //up
        y = (downHeight - upHeight) / 2 + 1 + middleHeight / 2
        sprite.setFlip(i == 1, false)
        addGraphic(x, y, width, height, sprite)

        //down
        y = (downHeight - upHeight) / 2 - height - 1 - middleHeight / 2
        sprite.setFlip(i == 1, true)
        addGraphic(x, y, width, height, sprite)

        if (i == 0) {
          body = new PolygonBody(
            Array(0, 0, trenchCornerWidth, cornerHeight,
              trenchCornerWidth, cornerHeight + middleHeight,
              0, cornerHeight * 2 + middleHeight), trenchCornerWidth / 2)
          body.setOrigin(trenchCornerWidth / 3 * 4, (cornerHeight * 2 + middleHeight) / 2)
          body.setPosition(x, y)
        } else {
          body = new PolygonBody(
            Array(0, 0,
              trenchCornerWidth, -cornerHeight,
              trenchCornerWidth, cornerHeight + middleHeight,
              0, middleHeight),
            trenchCornerWidth / 2)
          body.setOrigin(-trenchCornerWidth / 3, middleHeight / 2)
          body.setPosition(x, y + cornerHeight)
        }

        addBlock(body)

      }


    }


    //a bit prettier way to add triangle graphic element with block
    def addTriangleElement(x: Float, y: Float, width: Float, height: Float,
                           sprite: Sprite, flipX: Boolean, flipY: Boolean): Unit = {
      sprite.setFlip(flipX, flipY)
      addGraphic(x, y, width, height, sprite)

      if (!flipX && !flipY) addBlock(PolygonBody.triangleCollBody(x, y, width, 0, 0, height))
      else if (flipX && !flipY) addBlock(PolygonBody.triangleCollBody(x, y, width, 0, width, height))
      else if (!flipX && flipY) addBlock(PolygonBody.triangleCollBody(x, y, width, height, 0, height))
      else if (flipX && flipY) addBlock(PolygonBody.triangleCollBody(x, y + height, width, -height, width, 0))
    }


    //a bit prettier way to add elements + clones the sprite
    def addGraphic(x: Float, y: Float, width: Float, height: Float,
                   sprite: Sprite, borderX: Float = 0, borderY: Float = 0): Unit = {
      elements += new BorderSprite(Vector2e(x, y), Vector2e(width, height),
        Vector2e(borderX, borderY), new Sprite(sprite))
    }

    //a bit prettier way to add a collision block
    def addBlock(collisionBody: CollisionBody): Unit = {
      collObjects += new CollisionObject(this, physWorld, collisionBody)
    }

    //calculates the middle pos of the element n
    def blockPosYMiddle(n: Int): Float =
      dimensions.maxDown + blockHeight.take(n).sum + blockHeight(n) / 2f

    //calculates the middle pos of the element n
    def blockPosXMiddle(n: Int): Float =
      dimensions.maxLeft + blockWidth.take(n).sum + blockWidth(n) / 2f

    //calculates the pos of the element n
    def blockPosY(n: Int): Float =
      dimensions.maxDown + blockHeight.take(n).sum

    //calculates the pos of the element n
    def blockPosX(n: Int): Float =
      dimensions.maxLeft + blockWidth.take(n).sum


  }
}
