/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package geom

/** A base trait for DistCurveSeg and DistCurveTail and their associated GraphicElemsDist (these haven't been implemented or precisely named yet). */
trait DistCurveSegLike
{ /** Set to Double.NaN if LineSegment. Set to Double.Positive Infinity of ArcSegment, otherwise the x component of the the first bezier control
    *  point. */
  def xC1Metres: Double
  final def xC1: Dist = Dist(xC1Metres)
  def yC1Metres: Double
  final def yC1: Dist = Dist(yC1Metres)
  final def pC1: Dist2 = Dist2(xC1, yC1)
   
  def xUsesMetres: Double
  /** the x component of the uses point */
  def xUses: Dist = Dist(xUsesMetres)
  def yUsesMetres: Double
  /** the y component of the uses point */
  def yUses: Dist = Dist(yUsesMetres)
  /** the x component of the end point */
  /** The uses point. The centre point on an arc segment, control point 2 on a cubic bezier. Not used on line segment. */
  final def pUses: Dist2 = Dist2(xUses, yUses)
   
  def xEndMetres: Double
  def yEndMetres: Double
  def xEnd: Dist = Dist(xEndMetres)
  /** the y component of the end point */
  def yEnd: Dist = Dist(yEndMetres)
  /** The end point. Often called p2 on a line or p4 on a cubic bezier. */
  final def pEnd: Dist2 = Dist2(xEnd, yEnd)
}

/** A curve segment tail described in distance units rather than scalars. A DistCurveSeg without its starting point which will normally be supplied
 * by the preceding DistCurveTail. */
class DistCurveTail(val iMatch: Double, val xC1Metres: Double, val yC1Metres: Double, val xUsesMetres: Double, val yUsesMetres: Double,
                    val xEndMetres: Double, val yEndMetres: Double) extends ProdDbl7 with DistCurveSegLike
{ 
  def toCurveSeg(f: Dist2 => Vec2): CurveTail = xC1Metres match
  {
    case 10 =>
    { val endVec = f(pEnd)
      new CurveTail(10, 0, 0, 0, 0, endVec.x, endVec.y)
    }
    
    case 11 =>
    { val cenVec = f(pUses)
      val endVec = f(pEnd)
      new CurveTail(11, 0, 0, cenVec.x, cenVec.y, endVec.x, endVec.y)
    }
    
    case _ =>
    { val c1Vec = f(pC1)
      val cenVec = f(pUses)
      val endVec = f(pEnd)
      new CurveTail(12, c1Vec.x, c1Vec.y, cenVec.x, cenVec.y, endVec.x, endVec.y)
    }
  }
  override def canEqual(other: Any): Boolean = other.isInstanceOf[CurveTail]
  @inline override def _1 = iMatch
  @inline override def _2 = xC1Metres
  @inline override def _3 = yC1Metres
  @inline override def _4 = xUsesMetres
  @inline override def _5 = yUsesMetres
  @inline override def _6 = xEndMetres
  @inline override def _7 = yEndMetres
}

object LineSegDist
{ def apply(endPt: Dist2): DistCurveTail = new DistCurveTail(10, 0, 0, 0, 0, endPt.xMetres, endPt.yMetres)
//   override def toVec2s(f: Dist2 => Vec2): CurveSeg = LineSeg(f(endPt))   
}

object ArcSegDist
{
   def apply(cenPt: Dist2, endPt: Dist2): DistCurveTail =
      new DistCurveTail(11, 0, 0, cenPt.xMetres, cenPt.yMetres, endPt.xMetres, endPt.yMetres)
//   def toVec2s(f: Dist2 => Vec2): CurveSeg = ArcSeg(f(cenPt), f(endPt))
}
