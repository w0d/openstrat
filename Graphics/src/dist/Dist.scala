/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package geom
import math._

/** Distance in metres. */
final class Dist(val metres: Double) extends AnyVal with Ordered[Dist]
{ def typeStr: String = "Dist"
  //def str = persistD1(metres)
  def +(operand: Dist) = Dist(metres + operand.metres)
  def -(operand: Dist) = Dist(metres - operand.metres)
  def unary_- : Dist = Dist(-metres)
  def *(operand: Double): Dist = Dist(metres * operand)
  def /(operand: Double): Dist = Dist(metres / operand)
  def max(operand: Dist): Dist = ife(metres > operand.metres, this, operand)
  def min(operand: Dist): Dist = ife(metres < operand.metres, this, operand)
  def kmStr2 = (metres / 1000).str2 + "km"
  override def compare(that: Dist): Int = metres.match3(
    _ == that.metres, 0,
    _ > that.metres, 1,
    -1)

  def pos: Boolean = metres >= 0
  def neg: Boolean = metres < 0
}

/** Companion object for the Distance class. */
object Dist
{ def apply(metres: Double): Dist = new Dist(metres)

  implicit class DistImplicit(thisDist: Dist)
  { def * (operand: Dist): Area = new Area(thisDist.metres * operand.metres)
  }

  implicit object DistPersist extends PersistDbl1[Dist]("Dist", "metres",_.metres, new Dist(_))
}
