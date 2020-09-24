/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package geom

/** This is not a Polygon but should fTrans to Polygon. */
trait UnScaledPolygon //extends  UnScaled
{ type ThisT = PolygonGen
  def apply(): PolygonGen
  def fTrans(f: Vec2 => Vec2): ThisT = apply().fTrans(f)
  def dist(width: Dist, cen: Dist2 = Dist2Z): PolygonDist  = apply().distScale(width)
  def minX: Double = apply().minX
  def maxX: Double = apply().maxX
  def minY: Double = apply().minY
  def maxY: Double = apply().maxY
}

trait UnScaledPolygonYMirror extends UnScaledPolygon
{
  /* The right side of the Y Axis of this UnscaledPolygon, defined relative to a unit of 100 for convenience. So 0.35 is defined as 35. 0.222 is defined as 22.2  */
  def rtLine100: Vec2s
  final override def apply() = rtLine100.yMirrorClose.ySlate(-50).scale(0.01)
}