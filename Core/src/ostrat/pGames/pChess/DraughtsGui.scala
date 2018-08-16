/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pGames
package pChess
import geom._
import pGrid._
import pDisp._
import Colour._

case class DraughtsGui(canv: CanvasPlatform) extends CanvasSimple
{
   var player = true
//   def fSquare: OfSquareReg[CheckersSq, SideBare, CheckersBoard] => Disp2 = tog =>
//      {
//         import tog._
//         val colour: Colour = tile.colour        
//         val tv = vertDispVecs.fill(colour)
//         val ch = tile match {
//            case DarkSq(_, _, Some(b)) =>
//               {
//                  def colour = b.fold(Black, White)
//                  Some(Circle.fillSubj(cen, psc, colour.toString, colour))
//               }
//            case _ => None
//         }         
//         Disp2(List(tv), ch.toList)
//      }
    val darkSquareColour = Brown
    val lightSquareColour = Pink
    
    val rowSize = 8
    val rowCen = (1.0 + rowSize) / 2.0
    val margin = 15
    val tileWidth = ((height.min(width) - margin * 2).max(100) / rowSize)
    val tiles = for { y <- 1 to rowSize; x <- 1 to rowSize } yield Cood(x, y) match
    {
       case c @ Cood(x, y) if c.oddsOrEvens & y <= 3 => DarkSq(x, y, Some(true))
       case c @ Cood(x, y) if c.oddsOrEvens & y >= 6 => DarkSq(x, y, Some(false))
       case c @ Cood(x, y) if c.oddsOrEvens          => DarkSq(x, y, None)
       case c @ Cood(x, y)                           => LightSq(x, y)          
    }
    implicit class AdjInt(i: Int){ def adj = i - rowCen}
    val stuff = tiles.flatMap
    {
       case DarkSq(x, y, Some(b))  => List(             
             Square.fill(tileWidth, darkSquareColour, tileWidth * x.adj, tileWidth * y.adj),
             Circle.fillSubj(tileWidth / 1.6, "Hello", blackOrWhite(b), tileWidth * x.adj, tileWidth * y.adj)) 
       case DarkSq(x, y, _)  => Square.fill(tileWidth, darkSquareColour, tileWidth * x.adj, tileWidth * y.adj) :: Nil      
       case LightSq(x, y) => Square.fill(tileWidth, lightSquareColour, tileWidth * x.adj, tileWidth * y.adj) :: Nil          
    }
    repaint(stuff.toList)
   //def mapObjs: CanvObjs = ofSTilesDisplayFold(fSquare).collapse   
   //mapPanel.repaint(mapObjs)
//   mouseUp = (v, but: MouseButton, clickList) => (but, selected, clickList) match
//   {
//      case (LeftButton, _, _) =>
//         {
//            selected = clickList.fHead(Nil, (h , _) => List(h))
//            //statusText = selected.headOption.fold("Nothing Clicked")(_.toString)
//            //eTop()
//         }
//         case _ => //deb("Mouse other")
//   }    
}



