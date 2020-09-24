/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package geom

/** A Rectangle like final class aligned to the X and Y axes. */
trait Rectangularlign extends Rectangular
{ def width: Double
  def height: Double
  def xCen: Double
  def yCen: Double
  @inline final def xTopLeft: Double = xCen - width / 2
  @inline final def yTopLeft: Double = yCen + height / 2
  @inline final def topLeft: Vec2 = Vec2(xTopLeft, yTopLeft)
  @inline final def xTopRight: Double = xCen + width / 2
  @inline final def yTopRight: Double = yCen + height / 2
  @inline final def topRight: Vec2 = Vec2(xTopRight, yTopRight)
  @inline final def xBottomRight: Double = xCen + width / 2
  @inline final def yBottomRight: Double = yCen - height / 2
  @inline final def bottomRight: Vec2 = Vec2(xBottomRight, yBottomRight)
  @inline final def xBottomLeft: Double = xCen - width / 2
  @inline final def yBottomLeft: Double = yCen - height / 2
  @inline final def bottomLeft: Vec2 = Vec2(xBottomLeft, yBottomLeft)

}