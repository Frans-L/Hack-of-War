package game.loader

/** Holds the strings of the textures of the unit */
trait UnitTextures {
  val main: Seq[String] //seq index = player index
  val shadow: String
  val brightness: Float = 0.85f
}
