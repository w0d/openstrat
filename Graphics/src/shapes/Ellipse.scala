/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package geom
import pWeb._, Colour.Black, math.{Pi, sqrt}

/** The Ellipse trait can either be implemented as an [[Ellipse]] class or as a [[Circle]]. Which also fulfills the Ellipse interface. The factory
 *  methods in the Ellipse companion object return [Ellipse]]. */
trait Ellipse extends Shape with Curve
{
  def fill(fillColour: Colour): EllipseFill = EllipseFill(this, fillColour)

  override def draw(lineWidth: Double, lineColour: Colour = Black): EllipseDraw = EllipseDraw(this, lineWidth, lineColour)

  override def fillDraw(fillColour: Colour, lineWidth: Double, lineColour: Colour): GraphicElem = ???
  
  /** The x component of centre of the ellipse. */
  def xCen: Double

  /** The y component of centre of the ellipse. */
  def yCen: Double
  
  /** The centre of the ellipse. */
  final def cen: Vec2 = xCen vv yCen
  
  /** The x component of curvestill point 0. By default this will be the curvestill at the top of the Ellipse. */
  def xs0: Double

  /** The y component of curvestill point 0. By default this will be the curvestill at the top of the Ellipse. */
  def ys0: Double
  
  /** Curvestill point 0. By default this will be the curvestill at the top of the Ellipse. */
  def s0: Vec2

  /** The x component of curvestill point 1. By default this will be the curvestill at the right of the Ellipse. */
  def xs1: Double

  /** The y component of curvestill point 1. By default this will be the curvestill at the right of the Ellipse. */
  def ys1: Double

  /** Curvestill point 1. By default this will be the curvestill at the right of the Ellipse. */
  final def s1: Vec2 = xs1 vv ys1

  /** The x component of curvestill point 2. By default this will be the curvestill at the bottom of the Ellipse. */
  def xs2: Double

  /** The y component of curvestill point 2. By default this will be the curvestill at the bottom of the Ellipse. */
  def ys2: Double

  /** Curvestill point 2. By default this will be the curvestill at the bottom of the Ellipse. */
  final def s2: Vec2 = Vec2(xs2, ys2)

  /** The x component of curvestill point 3. By default this will be the curvestill at the right of the Ellipse. */
  def xs3: Double

  /** The y component of curvestill point 3. By default this will be the curvestill at the right of the Ellipse. */
  def ys3: Double

  /** Curvestill point 3. By default this will be the curvestill at the right of the Ellipse. */
  def s3: Vec2 = xs3 vv ys3

  /** radius 0. By default this will be the up radius to cs0. By convention and defualt This will normally be the value of b, the minor ellipse
   *  radius, but even if it starts as b in certain transformations it may become a, the major ellipse radius. */
  def radius0: Double

  final def diameter0: Double = radius0 * 2

  /** radius 1. This will normally be the value of a, the major ellipse radius, but even if it starts as a in certain transformations it may become b,
   *  the minor ellipse radius. */
  def radius1: Double

  final def diameter1: Double = radius1 * 2

  /** The major radius of this ellipse. */
  def a: Double

  /** The major radius of this ellipse. */
  def b: Double

  /** The h value of this ellipse. */
  def h: Double

  def ellipeRotation: Angle

  /** Eccentricity of ellipse. */
  def e: Double

  def area: Double
  def cxAttrib: XANumeric = XANumeric("cx", xCen)
  def cyAttrib: XANumeric = XANumeric("cy", yCen)
 // override def rotateRadians(radians: Double): Ellipse
  def rxAttrib: XANumeric = XANumeric("rx", radius1)
  def ryAttrib: XANumeric = XANumeric("ry", radius0)
  def attribs: Arr[XANumeric] = Arr(cxAttrib, cyAttrib, rxAttrib, ryAttrib)
  def boundingRect: BoundingRect

  def fTrans(f: Vec2 => Vec2): Ellipse = Ellipse.cs1s0(f(cen), f(s1), f(s0))

  /** Translate geometric transformation on a Ellipse returns a Ellipse. */
  override def slate(offset: Vec2): Ellipse = fTrans(_ + offset)

  /** Translate geometric transformation. */
  override def slate(xOffset: Double, yOffset: Double): Ellipse = fTrans(_.addXY(xOffset, yOffset))

  /** Uniform scaling transformation. The scale name was chosen for this operation as it is normally the desired operation and preserves Circles and
   * Squares. Use the xyScale method for differential scaling. */
  override def scale(operand: Double): Ellipse = fTrans(_ * operand)

  override def prolign(matrix: ProlignMatrix): Ellipse = fTrans(_.prolign(matrix))
  override def xyScale(xOperand: Double, yOperand: Double): Ellipse = fTrans(_.xyScale(xOperand, yOperand))
  override def rotateRadians(radians: Double): Ellipse = fTrans(_.rotateRadians(radians))

  override def reflectX: Ellipse = fTrans(_.reflectX)

  override def reflectY: Ellipse = fTrans(_.reflectY)

  override def reflectYOffset(xOffset: Double): Ellipse = ???

  override def reflectXOffset(yOffset: Double): Ellipse = ???

  override def reflect(line: Line): Ellipse = ???

  override def reflect(line: LineSeg): Ellipse = ???

  override def xShear(operand: Double): Ellipse = ???

  override def yShear(operand: Double): Ellipse = ???

}

object Ellipse
{ /** Factory method for an [[Ellipse]]. The apply factory methods in this Ellipse companion object default to an [[Implementation]] class. */
  def apply(radius1: Double, radius0: Double): Ellipse = new Implementation(0, 0, radius1, 0,  radius0)

  /** Factory method for an [[Ellipse]]. The apply factory methods in this Ellipse companion object default to an [[Implementation]] class. */
  def apply(radius1: Double, radius0: Double, cen: Vec2): Ellipse = new Implementation(cen.x, cen.y, cen.x + radius1, cen.y, radius0)

  /** The apply factory methods default to an [[Ellipse.Implementation]] Class. */
  /*def apply(radiusA: Double, radiusB: Double, xCen: Double, yCen: Double): Ellipse =
    new Implementation(xCen, yCen, xCen + radiusA, yCen, xCen, yCen + radiusB)*/

  def cs1s0(cen: Vec2, v1: Vec2, v0: Vec2): Implementation =
  { val radius0: Double = (v0 - cen).magnitude
    new Implementation(cen.x, cen.y, v1.x, v1.y, radius0)
  }

  implicit val slateImplicit: Slate[Ellipse] = (ell, offset) => cs1s0(ell.cen + offset, ell.s1 + offset, ell.s0 + offset)
  implicit val scaleImplicit: Scale[Ellipse] = (obj: Ellipse, operand: Double) => obj.scale(operand)
  implicit val rotateImplicit: Rotate[Ellipse] = (ell, radians) => Ellipse.cs1s0(ell.cen, ell.s1, ell.s0)

  /** The implementation class for Ellipses that are not Circles. The Ellipse is encoded as 3 Vec2s or 6 scalars although it is possible to encode an
   * ellipse with 5 scalars. Encoding the Ellipse this way greatly helps human visualisation of transformations upon an ellipse. */
  case class Implementation(xCen: Double, yCen: Double, xs1: Double, ys1: Double, radius0: Double) extends Ellipse
  { override def s0: Vec2 = cen + s0Angle.toVec2(radius0)
    override def xs0: Double = s0.x
    override def ys0: Double = s1.y
    override def xs2: Double = 2 * xCen - xs0
    override def ys2: Double = 2 * yCen - ys0

    def xs3: Double = 2 * xCen - xs1
    def ys3: Double = 2 * yCen - ys1

    override def radius1: Double = (s1 - cen).magnitude

    def a: Double = radius1.max(radius0)
    def b: Double = radius1.min(radius0)
    override def area: Double = Pi * radius1 * radius0
    override def e: Double = sqrt(a.squared - b.squared) / a
    override def h: Double = (a - b).squared / (a + b).squared  
    //override def fill(fillColour: Colour): ShapeFill = ???
    //override def fill(fillColour: Colour): EllipseCompound = EllipseCompound(this, Arr(FillFacet(fillColour)), Arr())
   // override def draw(lineWidth: Double, lineColour: Colour): EllipseDraw = ???

    def boundingRect: BoundingRect =
    { val xd0: Double = radius1.squared * (ellipeRotation.cos).squared + radius0.squared * (ellipeRotation.sin).squared
      val xd = xd0.sqrt
      val yd0: Double = radius1.squared * (ellipeRotation.sin).squared + radius0.squared * (ellipeRotation.cos).squared
      val yd = yd0.sqrt
      BoundingRect(xCen - xd, xCen + xd, yCen - yd, yCen + yd)
    }

    override def ellipeRotation: Angle = (s1 - cen).angle
    def s0Angle = ellipeRotation + 90.degs
  }
}