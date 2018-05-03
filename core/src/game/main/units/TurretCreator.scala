package game.main.units

import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies.{CircleBody, CollisionBody, PolygonBody}
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.elements.ai.AvoidObstacles
import game.main.gameworld.gameobject.elements.{ShadowElement, TextureElement, ai}
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.players.Player

object TurretCreator extends UnitCreator {

  override lazy val cost: Int = 5
  override lazy val texture: UnitTextures = GameTextures.Units.BaseTurret
  override lazy val width: Float = 100f
  override lazy val height: Float = 100f

  override def collBody: CollisionBody = new CircleBody(width / 2f)

  lazy val cannonTexture: UnitTextures = GameTextures.Units.BaseSoldier
  lazy val cannonWidth: Float = 100f / 1.75f
  lazy val cannonHeight: Float = 60f / 1.75f


  /** Sets the all stats to to unit */
  override protected def setStats(obj: UnitObject, path: Path): Unit = {

    obj.mass = 100
    obj.health = 1000
    obj.static = true

    obj.appendElement(createCannon())


    //creates the cannot over the base unit
    def createCannon(): UnitObject = {
      val cannon = new UnitObject(obj.physWorld, new CircleBody(5))

      cannon.owner = obj.owner
      cannon.pos.set(0, 0)
      cannon.size.set(cannonWidth, cannonHeight)
      cannon.origin.set(cannon.size.x / 2f, cannon.size.y / 2f)

      cannon.appendElement(
        new ShadowElement(GameTextures.default.atlas.findRegion(cannonTexture.shadow)))
      cannon.appendElement(
        new TextureElement(GameTextures.default.atlas.findRegion(cannonTexture.main(obj.owner.colorIndex))))

      val visionMaxDist = obj.sWidth * 4f
      val attackVision = new CircleBody(visionMaxDist)

      cannon.appendElement(new ai.ShootAhead(attackVision, 10f, 300, BasicBullet))
      cannon.appendElement(new ai.TurnToTarget(350f))

      cannon.angle = obj.angle

      cannon
    }


  }
}
