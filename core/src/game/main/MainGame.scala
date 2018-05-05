package game.main

import com.badlogic.gdx.graphics.g2d.{SpriteBatch, TextureAtlas}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.graphics.{Color, FPSLogger, GL20, OrthographicCamera}
import com.badlogic.gdx.utils.TimeUtils
import com.badlogic.gdx.utils.viewport.{ExtendViewport, Viewport}
import com.badlogic.gdx.{Gdx, Input, Screen}
import game.loader.GameTextures
import game.main.gameworld.gamemap
import game.main.gameworld.gameobject.ObjectHandler
import game.main.ui.GameUI
import game.util._
import game.main.players.{Bot, Player, User}
import game.main.unitcreators.bullet.BasicBullet


/**
  * Global variables makes debugging easier
  */
object MainGame {

  var drawCollBox: Boolean = false
  var debugViewPort: Viewport = _
  var debugRender: ShapeRenderer = _
}

/**
  * Created by Frans on 26/02/2018.
  */
class MainGame(textures: GameTextures, screenDim: Dimensions, returnAction: () => Unit) extends Screen {

  //sets the default ticker => everything should be time dependent
  private val ticker = new Ticker(TimeUtils.millis())
  Ticker.defaultTicker = ticker //to future gameElements
  ticker.speed = 1f

  //sets the default textures
  GameTextures.default = textures
  GameTextures.defaultUI = textures

  //sets the drawing batches
  private val batch: SpriteBatch = new SpriteBatch
  private val shapeRender: ShapeRenderer = new ShapeRenderer
  MainGame.debugRender = new ShapeRenderer
  MainGame.debugRender.setColor(0, 1f, 0f, 1)
  MainGame.drawCollBox = false

  //sets the game camera
  private val cam: OrthographicCamera = new OrthographicCamera()
  private val viewport: Viewport = new ExtendViewport(
    screenDim.width, screenDim.height,
    screenDim.maxWidth, screenDim.maxHeight, cam)
  viewport.apply()
  MainGame.debugViewPort = viewport

  //creates the object updater
  private val objectHandler: ObjectHandler = new ObjectHandler(screenDim)

  //adds the map
  private val map: gamemap.Map =
    new gamemap.Map(screenDim, textures, objectHandler)
  objectHandler.collHandler.map = map //TODO temporary solution to add map to physWorld

  //sets the players
  private val players: Seq[Player] = Seq(
    new User(objectHandler, 0),
    new Bot(objectHandler, 1))
  players.head.enemies += players.last
  players.last.enemies += players.head

  //sets the game timer
  private val gameTimer: TimerSecond = new TimerSecond(125)

  //sets the ui
  private val gameUI: GameUI =
    new GameUI(screenDim, viewport, players.head, shapeRender, gameTimer)

  private val fPSLogger: FPSLogger = new FPSLogger
  private val backgroundColor = Color.valueOf("#44535e")


  /**
    * Called each frame by libGDX => main loop
    */
  override def render(delta: Float): Unit = {
    //clears the screen
    Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    //draws the game
    this.draw()

    MainGame.debugRender.setProjectionMatrix(cam.combined) //Debug render
    MainGame.debugRender.begin(ShapeType.Line)
    this.updateDebug() //Debug controlls


    this.updateGameLogic() //update the game logic

    MainGame.debugRender.end() //Stop debug render
  }


  /**
    * Called each frame by render()
    **/
  private def draw(): Unit = {

    //update camera
    batch.setProjectionMatrix(cam.combined)

    //draw objects
    batch.begin()
    players.foreach(_.draw(batch))
    objectHandler.draw(batch)
    map.draw(batch)
    batch.end()

    //gameUI has own batch, no need to pass anything
    gameUI.draw(batch)

    //draw lines (mostly debug ones)
    shapeRender.setProjectionMatrix(cam.combined)
    shapeRender.begin(ShapeType.Line)
    shapeRender.setColor(0, 1f, 0f, 1)
    players.foreach(_.draw(shapeRender))
    objectHandler.draw(shapeRender)
    map.draw(shapeRender)
    gameUI.draw(shapeRender)
    shapeRender.end()

  }

  /**
    * Called each frame by render()
    **/
  private def updateGameLogic(): Unit = {

    //updates the timers
    ticker.update(TimeUtils.millis())
    gameTimer.update(ticker.delta)

    if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
      gameUI.showEndSplashScreen("RESIGNED", endGame)


    cam.update()
    players.foreach(_.update())
    objectHandler.update()
    gameUI.update()

    //checks if the game has the winner
    checkGameOver()
    
  }

  /** Updates viewport when resolution changes. */
  def resize(width: Int, height: Int): Unit = {
    viewport.update(width, height, true)
    cam.position.set(0, 0, 0)
    cam.update()
    gameUI.forceUpdate()
  }

  /** Ends the mainGame and calls the return action */
  def endGame(): Unit = {
    returnAction()
  }

  /** Calls endSplashScreen if needed*/
  private def checkGameOver(): Unit = {
    val winner = this.hasWinner
    winner.foreach(winner => {
      if (winner == players.head) gameUI.showEndSplashScreen("VICTORY", endGame)
      else gameUI.showEndSplashScreen("DEFEAT", endGame)
    })
    if (winner.isEmpty && gameTimer.isOver) gameUI.showEndSplashScreen("DRAW", endGame)
  }

  /** Returns the winner if it exists. */
  private def hasWinner: Option[Player] = {
    if (players.head.score <= 0 || (gameTimer.isOver && players.last.score > players.head.score))
      Some(players.last)
    else if (players.last.score <= 0 || (gameTimer.isOver && players.head.score > players.last.score))
      Some(players.head)
    else None
  }

  override def show(): Unit = Unit

  override def resume(): Unit = Unit

  override def pause(): Unit = Unit

  override def hide(): Unit = Unit

  override def dispose(): Unit = Unit


  private var debugSpacePressed = false

  /** Updates debug stuff... */
  def updateDebug(): Unit = {
    def target = MainGame.debugViewPort.unproject(Vector2e(Gdx.input.getX, Gdx.input.getY))

    if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
      if (!debugSpacePressed) {
        BasicBullet.create(players.head, objectHandler,
          target, Vector2e(-1, 0),
          players.last.colorIndex)
        BasicBullet.create(players.head, objectHandler,
          target, Vector2e(1, 0),
          players.last.colorIndex)
        BasicBullet.create(players.head, objectHandler,
          target, Vector2e(0, 1),
          players.last.colorIndex)
        BasicBullet.create(players.head, objectHandler,
          target, Vector2e(0, -1),
          players.last.colorIndex)
      }
      debugSpacePressed = true
    } else debugSpacePressed = false

    //Debug collBoxes
    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) MainGame.drawCollBox = !MainGame.drawCollBox

    //Debug printing
    if (ticker.interval4) Gdx.app.log("MainGame", "Render calls: " + batch.renderCalls)
    fPSLogger.log()
  }

}
