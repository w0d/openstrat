package ostrat
package pGrid
import geom._

/** A 2d line defined by its start and end TileGrid Roord. */
case class RoordLine(y1: Int, c1: Int, y2: Int, c2: Int) extends ProdInt4
{
  def _1 = y1
  def _2 = c1
  def _3 = y2
  def _4 = c2
  def toLine2(f: Roord => Vec2): Line2 =
  { val v1 = f(Roord(y1, c1))
    val v2 = f(Roord(y2, c2))
    Line2(v1, v2)
  }
}

object RoordLine
{ def apply(r1: Roord, r2: Roord): RoordLine = RoordLine(r1.y, r1.c, r2.y, r2.c)
}

/** An Array[Int] based collection for RoordLines. */
class RoordLines(val array: Array[Int]) extends AnyVal with ArrProdInt4[RoordLine]
{ type ThisT = RoordLines
  override def unsafeFromArray(array: Array[Int]): RoordLines = new RoordLines(array)
  override def typeStr: String = "RoordLines"
  override def newElem(i1: Int, i2: Int, i3: Int, i4: Int): RoordLine = RoordLine.apply(i1, i2, i3, i4)
  def toLine2s(f: Roord => Vec2): Line2s = pMap(_.toLine2(f))
  //override def toString: String = RoordLines.PersistImplicit.show(this)
}

class RoordLinesBuff(val buffer: Buff[Int] = buffInt()) extends AnyVal with ProdInt4Buff[RoordLine, RoordLines]
{// override def unBuff: RoordLines = new RoordLines(toArray)
  override def intsToT(i1: Int, i2: Int, i3: Int, i4: Int): RoordLine = new RoordLine(i1, i2, i3, i4)
}

object RoordLines extends ProdInt4sCompanion[RoordLine, RoordLines]
{ implicit val factory: Int => RoordLines = i => new RoordLines(new Array[Int](i * 4))
  override def buff(initialSize: Int): RoordLinesBuff = new RoordLinesBuff(buffInt(initialSize * 4))

  implicit val PersistImplicit = new ProductI4sBuilder[RoordLine, RoordLines]("RoordLines")
  { override def fromArray(value: Array[Int]): RoordLines = new RoordLines(value)
  }
}