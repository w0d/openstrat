/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pEarth
import pGrid._, Colour._

trait WTile extends WithColour
{
  def str: String
}

object WTile
{
  implicit object TerainIsType extends IsType[WTile]
  {
    override def isType(obj: AnyRef): Boolean = obj.isInstanceOf[WTile]
    override def asType(obj: AnyRef): WTile = obj.asInstanceOf[WTile]
  }

  implicit object TerrainPersist extends PersistSimple[WTile]("Terrain")
  {
    def show(obj: WTile): String = obj.str
    def fromExpr(expr: ParseExpr): EMon[WTile] = ???
  }

  val plain: WTile = Inland(Plains)
  val hills : WTile = Inland(Hilly)
  val forr : WTile = Inland(Plains, Forrest)
  val desert : WTile = Inland(Plains, Desert)
  val jungle : WTile = Inland(Plains, Jungle)
  val taiga : WTile = Inland(Plains, Taiga)
  val ice : WTile = Inland(Plains, IceCap)
  val sice: WTile = SeaIce
  val sea: WTile = Ocean
  val mtain: WTile = Inland(Mountains)
}

trait Water extends WTile
{
  def colour = Blue
}

case object Ocean extends Water { def str = "Ocean" }
case object Lake extends Water { def str = "Lake" }


object TerrainNone extends WTile
{ override def str = "No terrain"
  override def colour = Gray
}

trait Land extends WTile
{ def terr: Terrain
  def biome: Biome
  override def toString = biome.toString -- str

  def colour: Colour = terr match
  { case Plains => biome.colour
    case Hilly if biome == Forrest => Olive
    case Hilly => Chocolate
    case Mountains => Gray
  }
}

case class Inland(terr: Terrain, biome: Biome = OpenTerrain) extends Land
{
  def str = terr match
  { case Plains => biome.str
    case _ => terr.str
  }
}

trait Terrain
{ def str: String
  def colour: Colour
}

case object Plains extends Terrain
{ override def str = "Plains"
  override def colour: Colour = White
}

case object Hilly extends Terrain
{ override def str = "Hilly"
  override def colour = Chocolate
}

case object Mountains extends Terrain
{ override def str = "Mountain"
  override def colour = Gray
}

trait Biome
{ def colour: Colour
  def str: String
}

case object OpenTerrain extends Biome
{ def colour: Colour = LightGreen
  def str = "open ground"
}

case object Forrest extends Biome
{ override def str = "Forrest"
  override def colour = Green
}

case object Desert extends Biome
{ override def str = "Desert"
  override def colour = LemonChiffon
}

object Jungle extends Biome
{ override def str = "Jungle"
  override def colour = DarkGreen
}

object IceCap extends Biome
{ override def str = "IceCap"
  override def colour = White
}

object SeaIce extends WTile
{ override def str = "SeaIce" 
  override def colour = White
}

case object Taiga extends Biome
{ override def str = "Taiga"
  override def colour = DarkCyan
}

trait CoastLike{ def vertOffs: VertOffs}

class Coast(val terr: Terrain, val biome: Biome, val vertOffs: VertOffs) extends Land with CoastLike
{ def str = "Coast"
}
object Coast
{
  def apply(terr: Terrain = Plains, biome: Biome = OpenTerrain, upA: Int = 0, upB: Int = 0, UpRtA: Int = 0, upRtB: Int = 0,
    dnRtA: Int = 0, dnRtB: Int = 0, downA: Int = 0, downB: Int = 0, dnLtA: Int = 0, dnLtB: Int = 0, upLtA: Int = 0, upLtB: Int = 0): Coast =
    new Coast(terr, biome, VertOffs(upA, upB, UpRtA, upRtB, dnRtA, dnRtB, downA, downB, dnLtA, dnLtB, upLtA, upLtB))
}


class Coastal(val vertOffs: VertOffs) extends Water with CoastLike { def str = "Ocean"}
object Coastal
{
  def apply(upA: Int = 0, upB: Int = 0, upRtA: Int = 0, upRtB: Int = 0, dnRtA: Int = 0, dnRtB: Int = 0, downA: Int = 0, downB: Int = 0,
    dnLtA: Int = 0, dnLtB: Int = 0, upLtA: Int = 0, upLtB: Int = 0): Coastal =
    new Coastal(VertOffs(upA, upB, upRtA, upRtB, dnRtA, dnRtB, downA, downB, dnLtA, dnLtB, upLtA, upLtB))
}

case class Straits(farV: Int)