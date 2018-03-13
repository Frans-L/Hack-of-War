package game.objects

import com.badlogic.gdx.graphics.g2d.Sprite
import game.GameElement

/**
  * Created by Frans on 26/02/2018.
  */
abstract class GameObject extends GameElement {

  var visible: Boolean = true
  var enabled: Boolean = true
  var deleted: Boolean = false

  var posX, posY: Float
  var width, height: Float
  var scaleX, scaleY: Float = 1f
  var angle: Float = 0f //degrees

  var sprite: Sprite

  //Updates sprite location, rotation and size
  def updateSprite(): Unit = {
    sprite.setBounds(posX, posY, width, height)
    sprite.setScale(scaleX, scaleY)
    sprite.setRotation(angle)
    sprite.setOriginCenter()
  }

  //Returns scaled width
  def sWidth: Float = width * scaleX

  //Returns scaled height
  def sHeight: Float = width * scaleX

  //Get X location next to this object
  def nextToX: Float = posX + sWidth

  //Get Y location next to this object
  def nextToY: Float = posY + sHeight

}
