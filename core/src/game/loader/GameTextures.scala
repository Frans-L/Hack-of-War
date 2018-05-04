package game.loader

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.AssetLoader
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter
import com.badlogic.gdx.graphics.g2d.{BitmapFont, TextureAtlas}

/**
  * Created by Frans on 01/03/2018.
  */
class GameTextures extends Loadable {

  val load: Vector[(String, Class[_])] = Vector((GameTextures.atlasName, classOf[TextureAtlas]))

  val loadFont: Vector[(String, Class[BitmapFont], FreeTypeFontLoaderParameter)] = Vector(
    (GameTextures.Font.Normal.name, classOf[BitmapFont], GameTextures.Font.Normal.params),
    (GameTextures.Font.Big.name, classOf[BitmapFont], GameTextures.Font.Big.params)
  )

  var atlas: TextureAtlas = _

  object Fonts {
    var normal: BitmapFont = _
    var big: BitmapFont = _
  }

  override def finished(manager: AssetManager): Unit = {
    atlas = manager.get(load(0)._1)
    Fonts.normal = manager.get(loadFont(0)._1)
    Fonts.big = manager.get(loadFont(1)._1)
  }


}


object GameTextures {


  //null value at start =>
  //errors will appear when game starts, if this not set to be something
  var default: GameTextures = _
  var defaultUI: GameTextures = _

  val atlasName = "graphics.atlas"

  object Font {

    object Normal {
      val name = "normal.ttf"
      val params = new FreeTypeFontLoaderParameter()
      params.fontFileName = "fonts/DoHyeon.ttf"
      params.fontParameters.size = 45
      params.fontParameters.color = Color.WHITE
      params.fontParameters.shadowColor = Color.BLACK
      params.fontParameters.shadowOffsetX = 2
      params.fontParameters.shadowOffsetY = 2
      params.fontParameters.minFilter = TextureFilter.MipMapLinearLinear
      params.fontParameters.magFilter = TextureFilter.Linear
      params.fontParameters.genMipMaps = true
    }

    object Big {
      val name = "big.ttf"
      val params = new FreeTypeFontLoaderParameter()
      params.fontFileName = "fonts/DoHyeon.ttf"
      params.fontParameters.size = 90
      params.fontParameters.color = Color.WHITE
      params.fontParameters.shadowColor = Color.BLACK
      params.fontParameters.shadowOffsetX = 2
      params.fontParameters.shadowOffsetY = 2
      params.fontParameters.minFilter = TextureFilter.MipMapLinearLinear
      params.fontParameters.magFilter = TextureFilter.Linear
      params.fontParameters.genMipMaps = true
    }

  }

  object UI {

    val manaBar = "manaBar"
    val manaBarFill = "healthBarFill"

    val endBackground = "healthBarFill"

  }

  object Units {

    object BaseSoldier extends UnitTextures{
      override val main: Seq[String] =  Seq("unit1Blue", "unit1Red")
      override val shadow: String = "unit1Shadow"
    }

    object BaseBullet extends UnitTextures{
      override val main: Seq[String] = Seq("bullet1Blue", "bullet1Red")
      override val shadow: String = "bullet1Shadow"
    }

    object BaseTurret extends UnitTextures{
      override val main: Seq[String] = Seq("turret1Blue", "turret1Red")
      override val shadow: String = "turret1Shadow"
    }

    object LongTurret extends UnitTextures{
      override val main: Seq[String] = Seq("turretLongBlue", "turretLongRed")
      override val shadow: String = "turretLongShadow"
    }

    val healthBar = "healthBar"
    val healthBarFill = "healthBarFill"

    val card1 = "card1"
    val mapBorderShort = "mapBorderShort"
    val mapBorderWide = "mapBorderWide"

    val mapBorder = "mapBorder"
    val mapCorner = "mapCorner"
    val mapTrenchCorner = "mapTrenchCorner"
    val mapMiddleCorner = "mapMiddleCorner"

    val pathPoint = "pointCircle"
  }

}
