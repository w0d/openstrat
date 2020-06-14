/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package geom

case class EllipseClass(xCen: Double, yCen: Double , x1: Double, y1: Double, val bRadius: Double) extends Ellipse
{ def x2: Double = 2 * xCen - x1
  def y2: Double = 2 * yCen - y1
  def aRadius: Double = (v1 - cen).magnitude
  //override def shear(xScale: Double, yScale: Double): EllipseGen = this
  //override def rotate(angle: Angle): EllipseGen = this
  //override def rotateRadians(radians: Double):  EllipseGen = this
  //override def slateOld(offset: Vec2): EllipseGen = EllipseGen(cen + offset, vRight + offset, upRadius)
  //override def mirror(line: Line2): EllipseGen = EllipseGen(cen.mirror(line), vRight.mirror(line), upRadius)
 
  def mirrorX: TransElem = copy(x1, -y1, x2, -y2)
  def mirrorXOffset(yOffset: Double): TransElem = ???
  def mirrorY: TransElem = copy(-x1, y1, -x2)
  def mirrorYOffset(xOffset: Double): TransElem = ???
  def prolign(matrix: ProlignMatrix): TransElem = ???
  def scale(operand: Double): EllipseClass = Ellipse(xCen * operand, yCen * operand, x1 * operand, y1 * operand, bRadius * operand )
  def slate(xOffset: Double, yOffset: Double): EllipseClass = Ellipse(xCen + xOffset, yCen + yOffset, x1 + xOffset, y1 + yOffset, bRadius)
  def slate(offset: Vec2): TransElem = Ellipse(cen + offset, v1 + offset, bRadius)

  /** Rotates 90 degrees or Pi/2 radians clockwise. */
  override def rotate90: EllipseClass = ???

  override def rotate180: EllipseClass = ???

  override def rotate270: EllipseClass = ???

  override def rotateRadians(radians: Double): EllipseClass = ???

  override def fill(colour: Colour): EllipseFill = EllipseFill(this, colour)

  override def draw(lineWidth: Double, lineColour: Colour): ShapeDraw = ???

  override def mirror(line: Line2): EllipseClass = ???
}

object EllipseClass
{ def apply(vLeft: Vec2, vRight: Vec2, rHeight: Double): EllipseClass = new EllipseClass(vLeft.x, vLeft.y, vRight.x, vRight.y, rHeight)
}

