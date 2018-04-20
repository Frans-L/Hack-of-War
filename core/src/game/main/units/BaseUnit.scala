package game.main.units

import game.loader.GameTextures

object BaseUnit {

  val texture: Seq[String] = GameTextures.Units.unit1

  //size info
  private val scale = 1.5f
  val width: Float = 100f / scale
  val height: Float = 75f / scale


}
