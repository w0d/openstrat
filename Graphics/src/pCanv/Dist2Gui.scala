/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pCanv
import geom._

/** Why is the called a Dist2LikeGui not a Dist2 Gui */
trait Dist2Gui extends MapGui
{
  /** The Distance represented by one pixel width / height on the screen */
  var scale: Dist
  val margin = 35
   
  //(canv.width.subMin(margin, 20) / mapWidth).min(canv.height.subMin(margin, 20) / mapHeight)
  def scaleMax: Dist
  def scaleMin: Dist //= scaleAlignedMin.min(10.millionMiles)

  var mapFocus: Dist2 = Dist2(0.km, 0.km)
  //@inline def setFocus(x: Distouble, y: Double): Unit = mapFocus = Vec2(x, y)
  
  def scaleAlignedMin: Dist = ??? //mapPanelDiameter / mapWidth.max(mapHeight).max(0.000001)
  def scaleRotatedMin: Dist = ??? //(mapWidth.squared + mapHeight.squared) / mapWidth.max(mapHeight).max(0.000001)
   
  val bZoomIn = clickButton("+", zoomInCmd)
  val bZoomOut = clickButton("-", zoomOutCmd)
  val zoomable: Arr[GraphicActiveAffine] = Arr(bZoomIn, bZoomOut)

  val bMapRotateClockwise = clickButton("\u21BB", MouseButton => { rotation += Angle(-20); repaintMap() } )
  val bMapRotateAntiClockwise = clickButton("\u21BA", Mousebutton => { rotation += Angle(20); repaintMap() } )
  // val focusAdjust = Seq(bFocusLeft, bFocusRight, bFocusUp, bFocusDown, bMapRotateClockwise, bMapRotateAntiClockwise)

  def zoomInCmd: MouseButton => Unit = mb =>  { scale = (scale / 1.5).max(scaleMin); repaintMap() }
  def zoomOutCmd: MouseButton => Unit = mb => { scale = (scale * 1.5).min(scaleMax); repaintMap() }

  /** Translates a point from map position to Canvas Display position */
  def toCanv(mapPoint: Dist2): Vec2 = (mapPoint - mapFocus).rotate(rotation) / scale
   
  /** Translates a point from Canvas Display position back to Map position */
  def invCanv(canvPoint: Vec2): Vec2 = ??? //(canvPoint / scale).rotate(-rotation) + mapFocus

  /** Translates an array of map points to an array of Canvas Display positions */
  def arrCanv(inp: Dist2s): PolygonGen = inp.pMap(toCanv(_))

  final def repaintMap(): Unit =
  { val o2 = mapObjs
    mapPanel.repaint(o2)
  }
   
  def reFocus(newFocus: Dist2): Unit =
  { mapFocus = newFocus
    repaintMap()
  }

  def adjFocus(adj: Dist2): Unit = reFocus(mapFocus + adj)
  var rotation: Angle = deg0

  implicit class ImpVec2InCanvasMap(thisVec2: Vec2)
  { def mapRotate: Vec2 = thisVec2.rotate(rotation)
    def antiMapRotate: Vec2 = thisVec2.rotate(- rotation)
  }
}