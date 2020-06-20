/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package geom
import pCanv._, pXml._

trait EllipseGraphic extends ShapeGraphic//
{ type ThisT <: EllipseGraphic
  override def shape: Ellipse
  def fTrans(newEllipse: Ellipse): ThisT
  
  /** Mirror, reflection transformation across the X axis. This method has been left abstract in GeomElemNew to allow the return type to be narrowed
   * in sub classes. */  
  override def mirrorX: ThisT = fTrans(shape.mirrorX)
  
  /** Translate geometric transformation. */
  override def slate(offset: Vec2): ThisT = fTrans(shape.slate(offset))

  /** Translate geometric transformation. */
  override def slate(xOffset: Double, yOffset: Double): ThisT = fTrans(shape.slate(xOffset, yOffset))
}

final case class EllipseFill(shape: Ellipse, fillColour: Colour) extends EllipseGraphic with ShapeFill
{ type ThisT = EllipseFill

  override def fTrans(newEllipse: Ellipse): EllipseFill = EllipseFill(newEllipse, fillColour)
  
  /** Renders this functional immutable GraphicElem, using the imperative methods of the abstract [[ostrat.pCanv.CanvasPlatform]] interface. */
  override def rendToCanvas(cp: CanvasPlatform): Unit = cp.ellipseFill(this)  

  /** Uniform scaling transformation. The scale name was chosen for this operation as it is normally the desired operation and preserves Circles and
   * Squares. Use the xyScale method for differential scaling. */
  override def scale(operand: Double): DisplayElem = ???

  /** Mirror, reflection transformation across the line x = xOffset, which is parallel to the X axis. */
  override def mirrorYOffset(xOffset: Double): DisplayElem = ???

  /** Mirror, reflection transformation across the line y = yOffset, which is parallel to the X axis. */
  override def mirrorXOffset(yOffset: Double): DisplayElem = ???  

  /** Mirror, reflection transformation across the X axis. This method has been left abstract in GeomElemNew to allow the return type to be narrowed
   * in sub classes. */
  override def mirrorY: DisplayElem = ???

  override def prolign(matrix: ProlignMatrix): DisplayElem = ???

  /** Rotates 90 degrees or Pi/2 radians anticlockwise. */
  override def rotate90: DisplayElem = ???

  /** Rotates 180 degrees or Pi radians. */
  override def rotate180: DisplayElem = ???

  /** Rotates 90 degrees or Pi/2 radians clockwise. */
  override def rotate270: DisplayElem = ???

  override def rotateRadians(radians: Double): DisplayElem = ???

  override def mirror(line: Line2): DisplayElem = ???

  override def scaleXY(xOperand: Double, yOperand: Double): DisplayElem = ???

  override def shearX(operand: Double): TransElem = ???

  override def attribs: Arr[Attrib] = ???
}