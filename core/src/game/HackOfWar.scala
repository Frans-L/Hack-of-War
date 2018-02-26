package game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera, Texture}
import com.badlogic.gdx.graphics.g2d.{Sprite, SpriteBatch}
import com.badlogic.gdx.physics.bullet.Bullet
import com.badlogic.gdx.utils.viewport._

class HackOfWar extends ApplicationAdapter {
  private var batch: SpriteBatch = _
  private var img: Sprite = _
  private var cam: OrthographicCamera = _
  private var viewPort: Viewport = _

  val world_width = 100
  val world_height = 100

  override def create() {

    batch = new SpriteBatch
    img = new Sprite(new Texture("ConceptLarge.png"))
    img.setPosition(0, 0)
    img.setSize(2280, 1440)

    val w = Gdx.graphics.getWidth
    val h = Gdx.graphics.getHeight

    //2280 x 1440
    cam = new OrthographicCamera()
    viewPort = new ExtendViewport(1920, 1080, 2280, 1440, cam)
    //viewPort = new FillViewport(300,300,cam)
    viewPort.apply()

    cam.position.set(cam.viewportWidth / 2 + 180, cam.viewportHeight / 2 + 180, 0)

  }

  override def render() {

    cam.update()
    batch.setProjectionMatrix(cam.combined)

    Gdx.gl.glClearColor(1, 0, 1, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    batch.begin()
    img.draw(batch)
    batch.end()
  }

  override def resize(width: Int, height: Int): Unit = {
    viewPort.update(width, height, true)
    cam.position.set(cam.viewportWidth / 2 - (cam.viewportWidth - 2280) / 2, cam.viewportHeight / 2 - (cam.viewportHeight - 1440) / 2, 0)
    cam.update()
  }
}