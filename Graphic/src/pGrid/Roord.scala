package ostrat
package pGrid

final class Roord private(val bLong: Long) extends AnyVal with ProdInt2
{ def y: Int = bLong.>>(32).toInt
  def c: Int = bLong.toInt
  def _1 = y
  def _2 = c
  def canEqual(a: Any) = a.isInstanceOf[Roord]
  override def toString: String = "Roord".appendSemicolons(y.toString, c.toString)
  def + (operand: Roord): Roord = Roord(y + operand.y, c + operand.c)
  def -(operand: Roord): Roord = Roord(y - operand.y, c - operand.c)
  def *(operand: Int): Roord = Roord(y * operand, c * operand)
  def /(operand: Int): Roord = Roord(y / operand, c / operand)

  def addYC(yOff: Int, cOff: Int): Roord = Roord(y + yOff, c + cOff)
  def subYC(yOff: Int, cOff: Int): Roord = Roord(y - yOff, c - cOff)
  def addY(operand: Int): Roord = Roord(y + operand, c)
  def addC(operand: Int): Roord = Roord(y, c + operand)
  def subY(operand: Int): Roord = Roord(y - operand, c)
  def subC(operand: Int): Roord = Roord(y, c - operand)

  def toHexTile: HexTile = HexTile(y, c)
}

object Roord
{ def apply(y: Int, c: Int): Roord = new Roord(y.toLong.<<(32) | (c & 0xFFFFFFFFL))
  def fromLong(value: Long): Roord = new Roord(value)
  def unapply(rd: Roord): Option[(Int, Int)] = Some((rd.y, rd.c))
  implicit object persistImplicit extends PersistInt2[Roord]("Rood", "y", _.y, "c", _.c, apply)

  implicit val roordsBuildImplicit = new ArrProdInt2Build[Roord, Roords]
  { type BuffT = RoordBuff
    override def fromIntArray(array: Array[Int]): Roords = new Roords(array)
    override def fromIntBuffer(inp: Buff[Int]): RoordBuff = new RoordBuff(inp)
  }
}


trait Tile
{
  def r: Int
  def xi: Int
}

case class HexTile(r: Int, xi: Int) extends Tile
{
  override def toString: String = "Tile".appendParenthSemis(r.toString, xi.toString)
}

class HTStep(val y: Int, c: Int)
object HTStepUR extends HTStep(2, 2)
object HTStepRt extends HTStep(0, 4)
object HTStepDR extends HTStep(-2, 2)
object HTStepDL extends HTStep(-2, -2)
object HTStepLt extends HTStep(0, -4)
object HTStepUL extends HTStep(2, -2)