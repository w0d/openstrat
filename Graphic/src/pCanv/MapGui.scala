/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pCanv
import geom._

/** A MapGui uses a CanvMulti. It has a main map panel and a top control panel. In addition translates between 2d game coordinates and 2d coordinates
 *  on the canvas. Allowing the canvas to display a moving, scalable 2d view though the Game map. The x coordinate increases from left to right, the
 *  y coordinate increases from bottom to top. */
abstract class MapGui(title: String) extends CanvasPanelled(title)
{
  val barWidth = 30
  val topPan: Panel = addPanel(Rectangle.fromTL(canv.width, barWidth, canv.topLeft), true)
  topPan.backColour = Colour.Gray

  topPan.mouseUp =
  { case (b, List(MouseButtonCmd(cmd)), _) => cmd.apply(b)
    case (_, l, _) => deb(l.toString)
  }
   
  def textBox(str: String, cmd: AnyRef) = Rectangle(10, 25).parentFillText(cmd, Colour.Gray, str, 15, Colour.White, LeftAlign)
  /**  repaints the top command bar */
  def reTop(commands: Arr[GraphicBoundedFull]): Unit = topPan.repaint(displayRowGraphics(topPan.cenLeft, commands))
  var statusText = "This is the status text."
  def status = textBox(statusText, None)
  val mapPanel: Panel = addPanel(Rectangle.fromBL(canv.width, canv.height - barWidth, canv.bottomLeft))
  def mapPanelDiameter = mapPanel.width.min(mapPanel.height).max(10)   
  def mapObjs: GraphicElemFulls
  def eTop(): Unit
  
  def setStatus(str: String): Unit = { statusText = str; eTop() }
}

