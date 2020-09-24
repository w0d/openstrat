/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package geom
import reflect.ClassTag

/** Type class for the rotation of objects of type T. */
trait Rotate[T]
{ def rotateRadiansT(obj: T, radians: Double): T
}

/** Companion object for the Rotate[T] type class, contains implicit instances for collections and other container classes. */
object Rotate
{
  implicit def transSimerImplicit[T <: SimilarPreserve]: Rotate[T] = (obj, radians) => obj.rotateRadians(radians).asInstanceOf[T]
  
  implicit def arrImplicit[A, AA <: ArrBase[A]](implicit build: ArrBuild[A, AA], ev: Rotate[A]): Rotate[AA] =
    (obj, radians) => obj.map(ev.rotateRadiansT(_, radians))

  implicit def functorImplicit[A, F[_]](implicit evF: Functor[F], evA: Rotate[A]): Rotate[F[A]] =
    (obj, radians) => evF.mapT(obj, evA.rotateRadiansT(_, radians))

  implicit def arrayImplicit[A](implicit ct: ClassTag[A], ev: Rotate[A]): Rotate[Array[A]] = (obj, radians) => obj.map(ev.rotateRadiansT(_, radians))
}

/** Extension class for instances of the Rotate type class. */
class RotateExtensions[T](value: T, ev: Rotate[T]) extends RotateGenExtensions [T]
{
  override def rotateRadians(radians: Double): T = ev.rotateRadiansT(value, radians)
  def rotate(angle: Angle): T = ev.rotateRadiansT(value, angle.radians)

  /** Produces a regular cross of a sequence of four of the elements rotated */
  def rCross: Seq[T] = List(value, rotate270, rotate180, rotate90)

  def rCrossArr[TT <: ArrBase[T]](implicit build: ArrBuild[T, TT]): TT =  rCross.toImut  
}

trait RotateGenExtensions[T]
{
  def rotateRadians(radians: Double): T
  def rotate(angle: Angle): T
  /** Rotates 90 degrees or Pi/2 radians anticlockwise. */
  def rotate90: T = rotate(deg90)

  /** Rotates 180 degrees or Pi radians. */
  def rotate180: T = rotate(deg180)

  /** Rotates 90 degrees or Pi/2 radians clockwise. */
  def rotate270: T = rotate(-deg90)

  /** Rotates 90 degrees or Pi / 2 radians clockwise. */
  def clk90: T = rotate270
 
  /** Rotates 15 degrees anti-clockwise or + Pi/12 */
  def rotate15: T = rotate(deg15)
  
  /** Rotates 30 degrees anti-clockwise or + Pi/6 */
  def rotate30: T = rotate(deg30)
  
  /** Rotates 45 degrees anti-clockwise or + Pi/4 */
  def rotate45: T = rotate(deg45)
  
  /** Rotates 60 degrees anti-clockwise or + Pi/3 */
  def rotate60: T  = rotate(deg60)
  
  /** Rotates 120 degrees anti-clockwise or + 2 * Pi/3 */
  def rotate120: T = rotate(deg120)
  
  /** Rotates 135 degrees anti-clockwise or + 3 * Pi/4 */
  def rotate135: T = rotate(deg135)
  
  /** Rotates 150 degrees anti-clockwise or + 5 * Pi/6 */
  def rotate150: T = rotate(deg150)
  
  /** Rotates 30 degrees clockwise or - Pi/3 */
  def clk30: T = rotate(-deg30)
  
  /** Rotates 45 degrees clockwise or - Pi/4 */
  def clk45: T = rotate(-deg45)
  
  /** Rotates 60 degrees clockwise or - Pi/3 */
  def clk60: T  = rotate(-deg60)

  /** Rotates 120 degrees clockwise or - 2 * Pi/3 */
  def clk120: T = rotate(-deg120)
  
  /** Rotates 135 degrees clockwise or - 3 * Pi/ 4 */
  def clk135: T = rotate(-deg135)
  
  /** Rotates 150 degrees clockwise or - 5 * Pi/ 6 */
  def clk150: T = rotate(-deg150)  
}
