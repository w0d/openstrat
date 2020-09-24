/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package geom

/** A 2 dimensional line segment measured in metres. A straight line between two points on a 2 dimensional flat surface. */
class DistLineSeg(xStartMetres: Double, yStartMetres: Double, xEndMetres: Double, yEndMetres: Double)
{ def xStart: Dist = Dist(xStartMetres)
  def yStart: Dist = Dist(yStartMetres)
  def xEnd: Dist = Dist(xEndMetres)
  def yEnd: Dist = Dist(yEndMetres)
  def ptStart: Dist2 = Dist2(xStart, yStart)
  def ptEnd: Dist2 = Dist2(xEnd, yEnd)
  def toLine2(f: Dist2 => Vec2): LineSeg = LineSeg(f(ptStart), f(ptEnd))
}
object DistLineSeg
{
  def apply(startDist2: Dist2, endDist2: Dist2): DistLineSeg =
    new DistLineSeg(startDist2.xMetres, startDist2.yMetres, endDist2.xMetres, endDist2.yMetres)
}