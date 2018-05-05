package game.main.unitcreators

import game.loader.{GameTextures, UnitTextures}
import game.main.gameworld.collision.bodies.{CircleBody, CollisionBody, PolygonBody}
import game.main.gameworld.gamemap.Path
import game.main.gameworld.gameobject.objects.elements.ShadowElement
import game.main.gameworld.gameobject.objects.elements.unit.{HealthBarElement, UnitTextureElement}
import game.main.gameworld.gameobject.objects.UnitObject
import game.main.players.Player


trait BuildingCreator extends SoldierCreator {

  protected def collBody: CollisionBody

  override def create(owner: Player,
                      x: Float, y: Float,
                      path: Path, extraOffset: Float): Seq[UnitObject] = {

    val obj = UnitCreator.createUnit(owner, collBody, width, height)
    obj.category = UnitObject.Category.building
    UnitCreator.addTextures(obj, texture, owner, 1.25f, 1.25f)
    UnitCreator.posToStartLoc(obj, path)

    setStats(obj, owner, path)
    Seq(obj)
  }

}

