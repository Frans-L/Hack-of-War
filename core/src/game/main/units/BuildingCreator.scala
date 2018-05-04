package game.main.units

import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies.{CircleBody, CollisionBody, PolygonBody}
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.elements.ShadowElement
import game.main.gameworld.gameobject.elements.unit.{HealthBarElement, UnitTextureElement}
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.players.Player


trait BuildingCreator extends SoldierCreator {

  protected def collBody: CollisionBody

  override def create(owner: Player,
                      x: Float, y: Float,
                      path: Path, random: Boolean): UnitObject = {

    val obj = UnitCreator.createUnit(owner, collBody, width, height)
    UnitCreator.addTextures(obj, texture, owner, 1.25f, 1.25f)
    UnitCreator.posToStartLoc(obj, path)

    setStats(obj, owner, path)
    obj
  }

}

object LaneBuilding extends BuildingCreator {

  override val cost: Int = 10

  override protected lazy val texture: UnitTextures = GameTextures.Units.BaseTurret
  override protected lazy val width: Float = 75f
  override protected lazy val height: Float = 75f

  override protected def collBody: CollisionBody = new CircleBody(width / 2f)

  override protected def setStats(obj: UnitObject, owner: Player, path: Path): Unit = {

    obj.mass = 100
    obj.health = 1000
    obj.static = true

    obj.appendElement(BuildingTurretSmall.create(owner, obj))
    obj.appendElement(new HealthBarElement(obj.health))
  }

}

object MainBuilding extends BuildingCreator {

  override val cost: Int = 20

  override protected lazy val texture: UnitTextures = GameTextures.Units.LongTurret
  override protected lazy val width: Float = 80f
  override protected lazy val height: Float = 200f

  override protected def collBody: CollisionBody = PolygonBody.rectangleCollBody(0, 0, width, height)

  override protected def setStats(obj: UnitObject, owner: Player, path: Path): Unit = {

    obj.mass = 100
    obj.health = 2000
    obj.static = true

    obj.appendElement(BuildingTurretMain.create(owner, obj))
    obj.lastElement[UnitObject].pos.set(0, height / 3f)
    obj.appendElement(BuildingTurretMain.create(owner, obj))
    obj.lastElement[UnitObject].pos.set(0, -height / 3f)

    obj.appendElement(new HealthBarElement(obj.health))
  }

}

