/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package geom
import math.Pi

/** Angle value class. Its particularly important not to use this class to represent Latitudes as the Angle class has a normal range +- 180 degrees,
 *  while Latitudes have a normal range +- 90 degrees. */
final class Angle private(val secs: Double) extends AnyVal with AngleLike with ProdDbl1
{
  @inline override def dblValue: Double = secs

  /** Creates a Vec2 from this Angle for the given scalar magnitude parameter. */
  def toVec2(magnitude: Double): Vec2 = Vec2(math.cos(radians) * magnitude, math.sin(radians) * magnitude)

  /** Gives the length of the circumference of the arc. */
  def arcLength(radius: Double): Double = radians * radius

  override def toString = degStr2
  def degStr2: String = degs.str2 + "\u00B0"

  def +(other: Angle) = Angle.radians(radians + other.radians)
  def -(other: Angle) = Angle.radians(radians - other.radians)
  
  /** returns an angle between -Pi and Pi */
  def angleTo(other: Angle): Angle = other.radians -radians match
  { case r if r > Pi => Angle.radians(r - Pi2)
    case r if r < -Pi => Angle.radians(Pi2 + r)
    case r => Angle.radians(r)
  }
  
  def addRadians(other: Double) = Angle.radians(radians + other)
  def subRadians(other: Double) = Angle.radians(radians - other)
  def * (factor: Double): Angle = Angle.radians(radians * factor)
  def / (factor: Double): Angle = Angle.radians(radians / factor)
  @ inline def unary_- : Angle = Angle.radians(- radians)
  /** This is gives the smaller of the bisection angles  */
  def bisect(operand: Angle) = Angle.radians(radians + angleTo(operand).radians / 2)
}

/** Angle Companion object. */
object Angle
{
  /** Factory method for Angle from number of degrees */
  @inline def apply(degrees: Double): Angle = new Angle(degrees * secsInDeg)

  /** Resets radians to between + and - Pi */
  @inline def resetRadians(radians: Double): Double =  radians %% Pi2 match
  { case r if r <= -Pi => Pi2 + r
    case r if r > Pi => r - Pi2
    case r => r
  }

  /** Resets radians to between + and - Pi */
  @inline def resetSecs(secs: Double): Double =  secs %% secsIn360Degs match
  { case r if r <= -secsIn180Degs => secsIn360Degs + r
    case r if r > secsIn180Degs => r - secsIn360Degs
    case r => r
  }

  @inline def radians(radians: Double): Angle = new Angle(resetRadians(radians) * 180 * secsInDeg / Pi)

  @inline def secs(value: Double): Angle = new Angle(value)
}

/** Efficient Immutable Array[Double] based collection class, with the Angle values stored as arc seconds. */
final class Angles(val arrayUnsafe: Array[Double]) extends AnyVal with ArrProdDbl1[Angle]
{ override type ThisT = Angles
  override def typeStr: String = "Angles"
  override def newElem(dblValue: Double): Angle = Angle.secs(dblValue)
  override def unsafeFromArray(array: Array[Double]): Angles = new Angles(array)
  override def fElemStr: Angle => String = _.toString
  /** Not sure about this method. */
  override def foreachArr(f: Dbls => Unit): Unit = ???
}

/** Companion object for [[Angles]] class. */
object Angles
{
  def apply(elems: Angle*): Angles =
  { val array: Array[Double] = new Array[Double](elems.length)
    elems.iForeach((a, i) => array(i) = a.secs)
    new Angles(array)
  }

  /** Sequence of the four cardinal angles, 0, -90, 180, 90 degrees in clockwise order. */
  val cross: Angles = Angles(0.degs, 270.degs, 180.degs, 90.degs)

  /** Sequence of the four cardinal angles rotated by 45 degrees, 45, -45, -135, 135 degrees in clockwise order. */
  val cross45: Angles = Angles(45.degs, 315.degs, 225.degs, 135.degs)
}