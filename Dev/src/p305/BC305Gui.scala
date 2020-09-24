/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package p305
import geom._, pCanv._, pEarth._

case class BC305Gui(canv: CanvasPlatform, scen: BcScen) extends EarthGuiOld("BC 305")
{
  override def saveNamePrefix = "BC305"
  override def scaleMax: Dist = 14000.km / mapPanelDiameter
  scale = scaleMax
  var lat: Latitude = 20.north
  var long: Longitude = 20.east
  val maxLat = 70.north
  val minLat = 0.north
  //def focus: LatLong = lat * long

  val tops: Arr[Area1] = EarthAreas.oldWorld
//   override def eTop(): Unit = reTop(Seq(bIn, bOut, bLeft, bRight,
//         bDown, bUp, bInv, status))
//   /** 4 methods below are incorrect */
//   def leftCmd: MouseButton => Unit = (mb: MouseButton) =>
//      { long = Longitude((long.radians - distDelta(mb)).min(100)); updateView() }  
//   def rightCmd: MouseButton => Unit = (mb: MouseButton) => 
//      { long = Longitude((long.radians + (distDelta(mb)).max(70.N.radians))); updateView }   
//   def downCmd: MouseButton => Unit = (mb: MouseButton) =>
//      { lat = Latitude((lat.radians - distDelta(mb)).min(0)); updateView() }   
//   def upCmd: MouseButton => Unit = (mb: MouseButton) =>
//      { lat = Latitude((lat.radians + distDelta(mb)).max(0)); updateView() } 
         
  val fHex: OfETile[BcTileOld, ESideOldOnly] => DisplayElems = etog =>
    { import etog._         
      val colour: Colour = tile.colour
      val poly = vertDispVecs.fillActive(colour, tile)
      
      val tileText: DisplayElems = ifScaleCObjs(68,
        { val strs: Arr[String] = Arr(yxStr, cenLL.degStr)
          TextGraphic.lines(strs, 10, cen, colour.contrastBW)//.toArraySeq
        })
      poly +: tileText
      }
   def fSide: OfESide[BcTileOld, ESideOldOnly] => DisplayElems = ofs => {
      import ofs._
      ifScaleCObjs(60, side.terr match
        {
          case SideNone => ifTiles((t1, t2) => t1.colour == t2.colour, (t1, _) => vertDispLine.draw(1, t1.colour.contrastBW))
          case Straitsold => Arr(vertDispLine.draw(6, Colour.Blue))
        })
   }   
         
   def ls: DisplayElems =
   { val gs: DisplayElems = scen.grids.flatMap(_.eGraphicElems(this, fHex, fSide))
     val as: DisplayElems = scen.tops.flatMap(a => a.disp2(this))
     gs ++ as
   }
   
   eTop()
   loadView()
   repaintMap()
}