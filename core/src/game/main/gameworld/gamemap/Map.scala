package game.main.gameworld.gamemap

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Batch, Sprite, TextureRegion}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.{MathUtils, Vector2}
import game.GameElement
import game.loader.GameTextures
import game.main.gameworld.collision.bodies
import game.main.gameworld.collision.bodies.PolygonBody
import game.main.gameworld.gameobject
import game.main.gameworld.gameobject.ObjectHandler.Level
import game.main.gameworld.gameobject.{GameObject, objects}
import game.main.gameworld.gameobject.objects.BorderSprite
import game.util.{Dimensions, Vector2e}

import scala.collection.mutable


object Map {

  val border: String = GameTextures.Units.mapBorder

  val corner: String = GameTextures.Units.mapCorner
  val trenchCorner: String = GameTextures.Units.mapTrenchCorner
  val middleCorner: String = GameTextures.Units.mapMiddleCorner

  val pathPoint: String = GameTextures.Units.pathPoint

}

/**
  * Created by Frans on 06/03/2018.
  */
class Map(val dimensions: Dimensions,
          textures: GameTextures,
          objectHandler: gameobject.ObjectHandler) extends GameElement {

  private val elements: mutable.Buffer[gameobject.GameObject] = mutable.Buffer[gameobject.GameObject]()

  private val collAccuracy = 20 //collisionAccuracy
  private val collMap = Array.ofDim[Boolean](
    dimensions.maxWidth / collAccuracy + 1,
    dimensions.maxHeight / collAccuracy + 1)

  val collObjects: mutable.Buffer[objects.PhysicsObject] = mutable.Buffer[objects.PhysicsObject]()
  var path: Seq[Path] = _ //will be set at initializeMap
  var pathReversed: Seq[Path] = _
  var turretPath: Seq[Path] = _
  var turretPathReversed: Seq[Path] = _

  var centerX: Float = 0 //the playable areas center
  var centerY: Float = 0 //initializeMap gives right coords

  val mapObject: GameObject = new GameObject()
  objectHandler.addObject(mapObject, Level.map, update = false)

  initializeMap()
  createCollisionMap()


  override def update(): Unit = ???

  override def draw(shapeRender: ShapeRenderer): Unit = Unit

  override def draw(batch: Batch): Unit = {
    //elements.foreach(_.draw(batch))
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

  /** Returns the closest path. */
  def getPath(x: Float, y: Float): Option[Path] = {
    if (!collide(x, y)) {
      if (y < centerY) Some(path.head)
      else Some(path.last)
    } else None
  }

  /** Returns a random path. */
  def randomPath: Path = path(MathUtils.random(path.length - 1))

  /** Returns a random reversed path. */
  def randomPathReversed: Path = pathReversed(MathUtils.random(pathReversed.length - 1))

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

    //update the info about the center
    centerY = blockPosYMiddle(blockHeight.length / 2)
    centerX = blockPosXMiddle(blockWidth.length / 2)

    addBasicElements()
    addCorners()
    addTrenches()
    addMiddle()
    addPath()

    elements.foreach(mapObject.appendElement) //add all elements to mapobject
    collObjects.foreach(mapObject.appendElement)

    //adds the path
    def addPath(): Unit = {

      val offset = 75f
      val startHeight = 30f

      val routeDown = Seq(
        Vector2e(blockPosX(1) + blockWidth(1) / 5, blockPosYMiddle(3) - startHeight),
        Vector2e(blockPosX(1) + blockWidth(1) / 1.75f, blockPosYMiddle(3) - startHeight),
        Vector2e(blockPosX(1) + blockWidth(1) / 1.75f, blockPosYMiddle(1) + blockHeight(2) / 2 / 1.2f + offset),
        Vector2e(blockPosX(2), blockPosYMiddle(1) + blockHeight(2) / 2 / 1.2f),
        Vector2e(blockPosX(3), blockPosYMiddle(1) + blockHeight(2) / 2 + blockHeight(1)),

        Vector2e(blockPosX(4), blockPosYMiddle(1) + blockHeight(2) / 2 + blockHeight(1)),
        Vector2e(blockPosX(5), blockPosYMiddle(1) + blockHeight(2) / 2 / 1.2f),
        Vector2e(blockPosX(6) - blockWidth(1) / 1.75f, blockPosYMiddle(1) + blockHeight(2) / 2 / 1.2f + offset),
        Vector2e(blockPosX(6) - blockWidth(1) / 1.75f, blockPosYMiddle(3) - startHeight),
        Vector2e(blockPosX(6) - blockWidth(1) / 5, blockPosYMiddle(3) - startHeight)

      )

      val routeUp = Seq(
        Vector2e(blockPosX(1) + blockWidth(1) / 5, blockPosYMiddle(3) + startHeight),
        Vector2e(blockPosX(1) + blockWidth(1) / 1.75f, blockPosYMiddle(3) + startHeight),
        Vector2e(blockPosX(1) + blockWidth(1) / 1.75f, blockPosYMiddle(5) - blockHeight(4) / 2 / 1.2f - offset),
        Vector2e(blockPosX(2), blockPosYMiddle(5) - blockHeight(4) / 2 / 1.2f),
        Vector2e(blockPosX(3), blockPosYMiddle(5) - blockHeight(4) / 2 - blockHeight(1)),

        Vector2e(blockPosX(4), blockPosYMiddle(5) - blockHeight(2) / 2 - blockHeight(1)),
        Vector2e(blockPosX(5), blockPosYMiddle(5) - blockHeight(2) / 2 / 1.2f),
        Vector2e(blockPosX(6) - blockWidth(1) / 1.75f, blockPosYMiddle(5) - blockHeight(4) / 2 / 1.2f - offset),
        Vector2e(blockPosX(6) - blockWidth(1) / 1.75f, blockPosYMiddle(3) + startHeight),
        Vector2e(blockPosX(6) - blockWidth(1) / 5, blockPosYMiddle(3) + startHeight)

      )

      path = Seq(new Path(routeDown, offset), new Path(routeUp, offset))
      pathReversed = Seq(new Path(routeDown, offset).reverse, new Path(routeUp, offset).reverse)

      val routeSize = routeDown.size

      //main turrets location and direction look at
      val turretMiddle = Seq(
        Vector2e(blockPosX(1) - cornerWidth / 2, blockPosYMiddle(3)), //location
        Vector2e(blockPosX(1), blockPosYMiddle(3)), //looking dir
        Vector2e(blockPosX(6), blockPosYMiddle(3)), //looking dir
        Vector2e(blockPosX(6) + cornerWidth / 2, blockPosYMiddle(3)) //location
      )

      //returns the pos path of the buildings on the lane
      def turretLaneLoc(route: Seq[Vector2], mult: Float) = Seq(
        route(2).cpy.add(0, offset * 2 * mult), //location
        route(4).cpy.add(0, offset * mult), //looking direction
        route(routeSize - 5).cpy.add(0, offset * mult), //looking direction
        route(routeSize - 3).cpy.add(0, offset * 2 * mult) //location
      )

      val turretUp = turretLaneLoc(routeUp, 1)
      val turretDown = turretLaneLoc(routeDown, -1)

      turretPath = Seq(new Path(turretMiddle, 0), new Path(turretDown, 0), new Path(turretUp, 0))
      turretPathReversed = Seq(new Path(turretMiddle, 0).reverse,
        new Path(turretDown, 0).reverse, new Path(turretUp, 0).reverse)

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
                   texture: TextureRegion, borderX: Float = 0, borderY: Float = 0): Unit = {
      elements += BorderSprite(Vector2e(x, y), Vector2e(width, height),
        Vector2e(borderX, borderY), new TextureRegion(texture))
    }

    //a bit prettier way to add a collision block
    def addBlock(collisionBody: bodies.CollisionBody): Unit = {
      collObjects += objects.CollisionObject(this, collisionBody, objectHandler)
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
