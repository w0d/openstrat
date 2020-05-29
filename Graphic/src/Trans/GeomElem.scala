/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package geom

/** A geometric element to which 2 dimensional geometric transformations can be applied. Not all elements preserve their full properties under all
 * transformations. So for example a circle is no longer a Circle after a Shear transformation, but remains an Ellipse.  */
trait GeomElem extends Any// with Product with Serializable
{ /** Translate geometric transformation. */
  def slate(offset: Vec2): GeomElem

  /** Translate geometric transformation. */
  def slate(xOffset: Double, yOffset: Double): GeomElem

  /** Uniform scaling transformation. The scale name was chosen for this operation as it is normally the desired operation and preserves Circles and
   * Squares. Use the xyScale method for differential scaling. */
  def scale(operand: Double): GeomElem

  /** Mirror, reflection transformation across the line x = xOffset, which is parallel to the X axis. */
  def mirrorYOffset(xOffset: Double): GeomElem

  /** Mirror, reflection transformation across the line y = yOffset, which is parallel to the X axis. */
  def mirrorXOffset(yOffset: Double): GeomElem

  /** Mirror, reflection transformation across the X axis. This method has been left abstract in GeomElemNew to allow the return type to be narrowed
   * in sub classes. */
  def mirrorX: GeomElem

  /** Mirror, reflection transformation across the X axis. This method has been left abstract in GeomElemNew to allow the return type to be narrowed
   * in sub classes. */
  def mirrorY: GeomElem

  def prolign(matrix: ProlignMatrix): GeomElem
}

/* A temporary element which will be merged with With GeomElem once GeomElemOld can be removed. */
trait GeomElemNew extends GeomElem with Product with Serializable
{
  
}