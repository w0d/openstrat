/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pGames
package pWW2
import geom._, pEarth._, pCanv._, pStrat._

case class WWIIGui(canv: CanvasPlatform, scen: WWIIScen) extends EarthAllGui("World War II")
{
  deb("Beginning WWIIGui")
  focusUp = true
  override def saveNamePrefix = "WW2"
  
  val fHex: OfETile[W2Tile, W2Side] => GraphicElems = etog =>
    {
      import etog._         
      val colour: Colour = tile.colour
      val poly = etog.vertDispVecs.fillSubj(tile, colour)
      //val sides = etog.ifScaleCObjs(60, ownSideLines.map(line => LineDraw(line, 1, colour.contrastBW)))
      val textOrUnit: GraphicElems = ifScaleCObjs(68, tile.lunits match
          {
            case ::(head, _) if tScale > 68 => List(UnitCounters.infantry(30, head, head.colour,tile.colour).slate(cen))
            case _ => 
            {
              val ls: List[String] = List(xyStr, cenLL.toString)                   
              TextGraphic.lines(cen, ls, 10, colour.contrastBW)                  
            }
         })
         poly :: textOrUnit
      }
    
   def fSide: OfESide[W2Tile, W2Side] => GraphicElems = ofs =>
     {
       import ofs._
       ifScaleCObjs(60, side.terr match
         {
           case SideNone => ifTiles((t1, t2) => t1.colour == t2.colour, (t1, _) => vertDispLine.draw(1, t1.colour.contrastBW))
           case Straits => vertDispLine.draw(6, Colour.Blue) :: Nil
         })      
   } 
   
   //def dSides: GraphicElems = ofSidesDisplayFold(fSide)//(OfHexSideReg.implicitBuilder(_, _, _))
      
   def ls: GraphicElems = 
   {
      val gs: GraphicElems = scen.grids.flatMap(_.eGraphicElems(this, fHex, fSide))
      val as: GraphicElems = scen.tops.flatMap(a => a.disp2(this) )
      as ::: gs// + b  
   }   
   mapPanel.mouseUp = (v, but: MouseButton, clickList) => (but, selected, clickList) match
   {
      case (LeftButton, _, _) =>
         {
            selected = clickList.fHead(Nil, List(_))         
            statusText = selected.headOption.fold("Nothing Clicked")(_.toString)
            eTop()
         }
      case (RightButton, List(army : Army), List(t: W2Tile)) =>
         {
            scen.tile(army.cood).lunits = scen.tile(army.cood).lunits.removeFirst(_ == army)
            t.lunits = army :: t.lunits
            army.cood = t.cood
            repaintMap  
         }      
      case _ => 
   }
   scale = 1.08.km
   eTop()
   loadView
   repaintMap
}