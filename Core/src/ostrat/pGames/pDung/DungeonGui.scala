/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pGames
package pDung
import geom._, pCanv._, pGrid._, SqCode._, Colour._

class DungeonGui(canv: CanvasPlatform) extends SquareGridGui[DTile, SideBare, DungeonGrid](canv, Dungeon1)
{ mapPanel.backColour = Black
  override def eTop(): Unit = reTop(guButs :+ status)
   
  def fSquare: OfSquareReg[DTile, SideBare, DungeonGrid] => Disp2 = tog =>
  { import tog._
    val colour: Colour = tile.colour
    val tv = vertDispVecs.fillSubj(tile, colour)
    val sides = ifScaleCObjs(60, ownSideLines.map(_.draw(1, colour.contrastBW)))
    val tText = ifScaleCObj(60, TextGraphic(cen, xyStr, 14, colour.contrastBW))
    val player = ifScaleOptObjs(60, tile.charac){charac =>
      val poly1 = Rectangle(1.5).insVerts(1, -0.25 vv 0.5, 0 vv 0.8, 0.25 vv 0.5)
      val poly2 = poly1.scale(tog.tScale / 2.5).rotate(charac.facing.angle).slate(tog.cen)      
      List(poly2.fillDrawSubj(charac, charac.colour, 1), TextGraphic(cen, charac.iden.toString, 16, charac.colour.contrast))
    }
    Disp2(List(tv), tText ++ player ++ sides)
  }
  def mapObjs: GraphicElems =  ofTilesDisplayFold[OfSquareReg[DTile, SideBare, DungeonGrid]](fSquare).collapse

  mapPanel.mouseUp = (v, but: MouseButton, clickList) => (but, selected, clickList) match
  {
    case (LeftButton, _, cl) =>
    { selected = clickList.fHead(Nil, List(_))
      statusText = selected.headOption.fold("Nothing Clicked")(_.toString)
      eTop()
    }
    case (RightButton, List(ch: Character), List(newTile: DTile)) if
      adjTileCoodsOfTile(ch.cood).contains(newTile.cood) && ch.canMove(newTile) =>
    { grid.getTile(ch.cood).charac = None
      ch.cood = newTile.cood
      newTile.charac = Some(ch)
      repaintMap      
    }
    case (MiddleButton, List(ch: Character), List(newTile: DTile)) => optFace(ch.cood, newTile.cood) match
    { case Some(face) => { ch.facing = face; repaintMap }      
      case _ => deb("Middle Button other")
    }
    case _ =>
  }
  eTop()
  mapPanel.repaint(mapObjs)
}