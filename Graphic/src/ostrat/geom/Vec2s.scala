package ostrat
package geom

/** Array[Double] based collection class for Vec2s. Use Polygon or LinePath to represent those structures. Conversion to and from Polygon class and
 *  LinePath class should not entail a runtime cost. */
class Vec2s(val arr: Array[Double]) extends AnyVal with ProductD2s[Vec2]  with Transer with Vec2sLike
{ override def typeStr: String = "Vecs2"
  //override def toString: String = Vec2s.Vec2sPersist.show(this)
  override def elemBuilder(d1: Double, d2: Double): Vec2 = Vec2.apply(d1, d2)
  @inline def lengthFull: Int = arr.length / 2
  @inline def xStart: Double = arr(0)
  @inline def yStart: Double = arr(1)
  @inline def pStart: Vec2 = Vec2(xStart, yStart)
  def toPolygon: Polygon = new Polygon(arr)

  def fTrans(f: Vec2 => Vec2): Vec2s =  new Vec2s(arrTrans(f))

  def foreachEnd(f: (Double, Double) => Unit): Unit =
  { var count = 1
    while (count < lengthFull)
    { f(arr(count *2), arr( count * 2 + 1))
      count += 1
    }
  }

  /** Closes the line Path into a Polygon, by mirroring across the yAxis. This is useful for describing symetrical across the y Axis polygons, with
   * the minimum number of points. */
  def yMirrorClose: Polygon =
  {
    val acc = new Array[Double](arr.length * 2)
    var count = 0
    while (count < arr.length)
    { acc(count) = arr(count)
      count += 1
    }
    count = 0
    while (count < length)
    {
      count += 1
    }


    new Polygon(acc)
  }

  //def draw(lineWidth: Double, colour: Colour = Colour.Black): Vec2sDraw = LinePathDraw(this, lineWidth, colour)
}

object Vec2s extends ProductD2sCompanion[Vec2, Vec2s]
{
  implicit val factory: Int => Vec2s = i => new Vec2s(new Array[Double](i * 2))

  implicit object Vec2sPersist extends ProductD2sBuilder[Vec2, Vec2s]("Vec2s")
  {
    override def fromArray(value: Array[Double]): Vec2s = new Vec2s(value)
  }
}