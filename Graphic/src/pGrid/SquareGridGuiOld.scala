/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pGrid

/** A Gui for a single regular SquareGridComplex. Currently there are no irregular SquareGrids */
abstract class SquareGridGuiOld[TileT <: TileOld, SideT <: TileSideOld, GridT <: SquareGridOld[TileT, SideT]](val canv: pCanv.CanvasPlatform,
  val grid: GridT, title: String) extends TileGridGui[TileT, SideT, GridT](title)
{  
   def ofSTilesFold[R](f: OfSquareReg[TileT, SideT, GridT] => R, fSum: (R, R) => R, emptyVal: R) =
   {
      var acc: R = emptyVal
      foreachTilesCoodAll{ tileCood =>
         val newTog = OfSquareReg(grid.getTile(tileCood), grid, this)
         val newRes: R = f(newTog)
         acc = fSum(acc, newRes)
      }
      acc
   }
} 