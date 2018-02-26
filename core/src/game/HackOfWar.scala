package game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera, Texture}
import com.badlogic.gdx.graphics.g2d.{Sprite, SpriteBatch}
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.utils.viewport._

class HackOfWar extends ApplicationAdapter {

  private var batch: SpriteBatch = _
  private var shapeRender: ShapeRenderer = _
  private var cam: OrthographicCamera = _
  private var viewPort: Viewport = _
  private var world: World = _

  override def create() {

    batch = new SpriteBatch
    shapeRender = new ShapeRenderer

    world = new World(1920, 1080, 2280, 1440)
    cam = new OrthographicCamera()
    viewPort = new ExtendViewport(world.width, world.height, world.maxWidth, world.maxHeight, cam)
    viewPort.apply()

  }

  override def render() {

    cam.update()
    batch.setProjectionMatrix(cam.combined)
    shapeRender.setProjectionMatrix(cam.combined)

    Gdx.gl.glClearColor(1, 1, 1, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    batch.begin()
    batch.end()

    shapeRender.begin(ShapeType.Line)

    shapeRender.setColor(1, 0, 1, 1)
    shapeRender.circle(0, 0, 25)
    shapeRender.rect(world.left, world.maxDown, world.width - 1, world.maxHeight - 1)
    shapeRender.rect(world.maxLeft, world.down, world.maxWidth - 1, world.height - 1)

    shapeRender.end()
  }

  override def resize(width: Int, height: Int): Unit = {
    viewPort.update(width, height, true)
    cam.position.set(0, 0, 0)
    cam.update()
  }
}