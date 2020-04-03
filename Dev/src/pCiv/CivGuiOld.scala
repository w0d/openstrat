/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pCiv
import geom._, pGrid._, pCanv._, Colour._

class CivGuiOld(canv: CanvasPlatform) extends HexGridGuiOld[CTileOld, SideOldBare, CivGridOld](canv, "Civs")
{
  statusText --= "Left click unit to select. Right click on adjacent hex to move."
  override val grid: CivGridOld = Civ1Old
  override def scaleMin = 10 
  var pScale: Double = scaleAlignMin
  var focus: Vec2 = grid.cen
  mapPanel.backColour = Colour.Black

  def  fHex: OfHexReg[CTileOld, SideOldBare, CivGridOld] => GraphicElems = tog =>
    {
      import tog._        
      val colour: Colour = tile.colour
      //val poly = tog.vertDispVecs
      val tv = vertDispVecs.fillActive(colour, tile)
      val sides = ifScaleCObjs(60, ownSideLines.map(_.draw(1, colour.contrastBW)))
      val tText = ifScaleCObj(60, TextGraphic(yxStr, 14, cen, colour.contrastBW))
      val sett = ifScaleIfCObj(40, tile.settlement, Circle(25).slate(cen).fillFixed(None, Black))

      val lunit: GraphicElems = tile.lunits match
      {
        case RefsHead(head) if tog.tScale > 50 =>
        { val maxOffset = tog.grid.coodToVec2(head.dirn.halfRelCood)
          val gridPosn = cenRelGrid + maxOffset * head.offsetMagnitude
          val posn = fTrans(gridPosn)
          val fillColour = head.faction.colour                      
          val r = Rectangle.curvedCornersCentred(90, 60, 10, posn).parentAll(head, fillColour, 2, fillColour.contrast, 16, head.movePts.toString)
          Refs(r)
        }
        case _ => Refs()
       }
       Refs(tv) ++ tText ++ sett ++ lunit ++ sides
     }

  def mapObjs: GraphicElems = ofHTilesDisplayFold(fHex)// ofHexsDisplayFold(fHex).collapse

  mapPanel.mouseUp = (but: MouseButton, clickList, v) => (but, selected, clickList) match
  {
    case (LeftButton, _, _) =>
    {
      deb(selected.toString)
      selected = clickList//.fHead(Arr(), Arr(_))
      statusText = selected.headToStringElse("Nothing Clicked")
      eTop()
    }
    
    //If a Warrior is selected and a tile adjacent to the Warrior is right clicked =>
    case (RightButton, List(warr : Warrior), List(newTile: CTileOld)) =>
      {
        deb("Rt") 
        val newCood = newTile.cood
        val oldCood = warr.cood
        
        if (grid.isTileCoodAdjTileCood(oldCood, newCood) && warr.movePts > 0)
        {
          warr.dirn = HexDirn.fromNeighbTileCood(newCood - oldCood)
          def out(elapsed: Integer, startTime: Integer): Unit =
          {
            warr.offsetMagnitude = elapsed / 600
            if (warr.offsetMagnitude > 2)
            {
              warr.offsetMagnitude = 0
              warr.dirn = HCen
              val oldTile = grid.getTile(oldCood)
              oldTile.lunits = oldTile.lunits.removeFirst(_ == warr)
              warr.cood = newCood
              newTile.lunits +:= warr
              repaintMap
            }
            else
            { repaintMap
              canv.frame(out, startTime, 15)
            }                  
          }
          
          warr.movePts = (warr.movePts - warr.terrCost(newTile)).max(0)
          canv.startFrame((el, st) => out(el, st), 15)               
       }            
    }
    case (RightButton, l, _) => deb(l.toString)   
    case _ => deb("Mouse other")
  }   

  def turnCmd: MouseCmd = mb => { foreachTileAll(_.lunits.foreach(_.resetMovePts())); repaintMap }
  val bTurn = clickButton("T", turnCmd)   
  override def eTop(): Unit = reTop(guButs +- bTurn +- status)
  eTop()
  mapPanel.repaint(mapObjs)
}