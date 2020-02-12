/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pCanv
import geom._

trait DispObj
sealed trait DispPhase
case class DispAnim(fAnim: Double => Unit, secs: Double) extends DispPhase
case class DispStill(fDisp: () => Unit) extends DispPhase

/** A virtual panel created from the Canvas implemented using the clip function. */
case class Panel(private val outerCanv: CanvasPanelled, clipPoly: Polygon, cover: Boolean = true) extends PanelLike
{
  override def toString: String = "Panel:" -- clipPoly.toString
  val cen: Vec2 = clipPoly.polyCentre
  def width = clipPoly.boundingWidth
  def height = clipPoly.boundingHeight
   
  def repaintOld(els: ArrOld[GraphicElem]): Unit = { canvObjs = els.toRefs; outerCanv.refreshPanel(this) }
  def repaint(els: GraphicElems): Unit = { canvObjs = els; outerCanv.refreshPanel(this) }
  def repaints(els: GraphicElem*): Unit = repaint(els.toRefs)
}

case class MButtonCmd(cmd: MouseButton => Unit)
{ @inline def apply(mb: MouseButton): Any = cmd(mb)
}

/** The purpose of this trait is to give common methods for Panels and Simple Canvases. A SimpleCanvas is like a Panel but not a Panel.*/ 
trait PanelLike extends RectGeom
{ /** These are currently stored in reverse. I think this would be better in an Array */
  var subjs: Refs[GraphicActive] = Refs()

  var canvObjs: GraphicElems = Refs()

  /** This method name is inconsistent with mouseup on the canvas class*/
  var mouseUp: (Vec2, MouseButton, Refs[AnyRef]) => Unit = (v, b, s) => {}

  /** This method name is inconsistent with mousedown on the canvas class */
  var mouseDown: (Vec2, MouseButton, AnyRefs) => Unit = (v, b, s) => {}

  var fMouseMoved: (Vec2, MouseButton, AnyRefs) => Unit = (v, b, s) => {}
  var fMouseDragged: (Vec2, MouseButton, AnyRefs) => Unit = (v, b, s) => {}
  def setMouseSimplest(action: => Unit): Unit = mouseUp = (_, _, _) => action
  def setMouseSimple(action: Vec2 => Unit): Unit = mouseUp = (v, _, _) => action(v)
}