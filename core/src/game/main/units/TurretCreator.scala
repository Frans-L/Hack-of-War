package game.main.units

import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies.{CircleBody, CollisionBody, PolygonBody}
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.elements.unit.{AvoidObstacles, HealthBarElement, UnitTextureElement}
import game.main.gameworld.gameobject.elements.{ShadowElement, TextureElement, unit}
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.players.Player

object TurretCreator extends UnitCreator {

  override lazy val cost: Int = 5
  override lazy val texture: UnitTextures = GameTextures.Units.BaseTurret
  override lazy val width: Float = 75f
  override lazy val height: Float = 75f

  override def collBody: CollisionBody = new CircleBody(width / 2f)

  lazy val cannonTexture: UnitTextures = GameTextures.Units.BaseSoldier
  lazy val cannonWidth: Float = 100f / 1.33f
  lazy val cannonHeight: Float = 70f / 1.33f


  /** Sets the sprites to units */
  override protected def setSprite(obj: UnitObject, owner: Player): Unit = {
    val main = new UnitTextureElement(
      GameTextures.default.atlas.findRegion(texture.main(owner.colorIndex)), texture.brightness)
    val shadow = new ShadowElement(GameTextures.default.atlas.findRegion(texture.shadow))
    //main.color.set(1,1,1,0.5f)
    //shadow.color.set(1,1,1,0.0f)
    obj.appendElement(shadow)
    obj.appendElement(main)

  }

  /** Sets the all stats to to unit */
  override protected def setStats(obj: UnitObject, path: Path): Unit = {

    obj.mass = 100
    obj.health = 1000
    obj.static = true

    obj.appendElement(createCannon())
    obj.appendElement(new HealthBarElement(obj.health))

    //creates the cannot over the base unit
    def createCannon(): UnitObject = {
      val cannon = new UnitObject(obj.physWorld, new CircleBody(5))

      cannon.owner = obj.owner
      cannon.pos.set(0, 0)
      cannon.size.set(cannonWidth, cannonHeight)
      cannon.origin.set(cannon.size.x / 3f, cannon.size.y / 2f)

      //cannon.appendElement(
      //  new ShadowElement(GameTextures.default.atlas.findRegion(cannonTexture.shadow)))
      //cannon.lastElement[ShadowElement].color.set(1, 1, 1, 0.5f)

      cannon.appendElement(
        new TextureElement(GameTextures.default.atlas.findRegion(cannonTexture.main(obj.owner.colorIndex))))
      cannon.lastElement[ShadowElement].color.set(1, 1, 1, 0.75f)

      val visionMaxDist = obj.sWidth * 4f
      val attackVision = new CircleBody(visionMaxDist)

      cannon.appendElement(new unit.ShootAhead(attackVision, 10f, 300, BasicBullet))
      cannon.appendElement(new unit.TurnToTarget(350f))

      cannon.angle = obj.angle

      cannon
    }


  }

}
