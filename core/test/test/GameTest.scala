package test

import com.badlogic.gdx.{Application, ApplicationListener, Gdx}
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.graphics.GL20
import org.junit.{AfterClass, BeforeClass}
import org.mockito.Mockito


/**
  * Created by using an awesome
  * tutorial: http://manabreak.eu/java/2016/10/21/unittesting-libgdx.html
  */
object GameTest {
  var application: Application = _

  //headless backend
  @BeforeClass
  def init(): Unit = {
    this.application = new HeadlessApplication(new ApplicationListener() {
      override def resume(): Unit = Unit

      override def pause(): Unit = Unit

      override def create(): Unit = Unit

      override def resize(width: Int, height: Int): Unit = Unit

      override def dispose(): Unit = Unit

      override def render(): Unit = Unit
    })

    // Use Mockito to mock the OpenGL methods since we are running headlessly
    Gdx.gl20 = Mockito.mock(classOf[GL20])
    Gdx.gl = Gdx.gl20
  }

  //clean up
  @AfterClass
  def cleanUp() {
    this.application.exit()
    this.application = null
  }

}

class GameTest {

}
