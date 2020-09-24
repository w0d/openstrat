/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
import geom._

/** This package works with hexagonal and Square tile grids. The tile objects themselves will not in the general case the contain grid coordinates, although
 * it may be necessary to include this data for complex Tile values interacting with their wider environment. Its fundamental components are the grid data itself.
 * This is just a linear array of tile data. Compile-time typed grid data. So for example a chess board can be represented by a 64 element Arr, its context
 * determines that it is to be interpreted as an 8 by 8 square grid. Grid descriptions that describe the grid representation in the Array and GridFunctions
 * which implement Cood to T. The grid and grid-gui hierarchies currently contain a mix of new and old systems.
 *
 * The package name pGrid was chosen to allow you to use the name "grid" in your code. */
package object pGrid
{
  val Cood00 = Cood(0, 0)

  /** Gives a Coods Seq of Cood along a horisonatal line */
  def hexSidesHorr(y: Int, cStart: Int, cEnd : Int): Roords =
  { val cs = if (cStart > cEnd) cStart.roundDownToOdd to cEnd.roundUpToOdd by -2 else cStart.roundUpToOdd to cEnd.roundDownToOdd by 2
    cs.pMap(c => Roord(y, c))
  }

  /** Gives a Coods Seq of Cood along a horisonatal line */
  @deprecated def hexSidesHorrOld(y: Int, xStart: Int, xEnd : Int): Coods =
  { val xs = if (xStart > xEnd) xStart.roundDownToOdd to xEnd.roundUpToOdd by -2 else xStart.roundUpToOdd to xEnd.roundDownToOdd by 2
    xs.pMap(x => Cood(x, y))     
  }
  
  /** Not sure about this method */
  @deprecated def rectHCoods(xDim: Int,  yDim: Int, xOff: Int = 0, yOff: Int = 0): Set[Cood] =
  { val res1 = for
    { x <- xOff until xDim * 2 + xOff by 2
      y <- yOff until yDim * 2 + yOff by 2
	    if x % 4 == y % 4	              
    }  
	  yield Cood(x + 2, y + 2)
    res1.toSet
  }
  
  implicit class IntGridImplicit(thisInt: Int)
  { /** Syntax for succinct  Cood notation. */
    @deprecated def cc (y: Int): Cood = Cood(thisInt, y)
    def rr (c: Int): Roord = Roord(thisInt, c)
  }

  implicit class ArrayImplicit[A](thisArray: Array[A])
  { def gridForeach(f: (Roord, A) => Unit)(implicit grid: TileGridSimple): Unit = grid.foreach{r => f(r, thisArray(grid.arrIndex(r)))}
  }

  implicit class GridTransAllExtension[T](value: T)(implicit grid: TileGridSimple, ev: AffineTrans[T])
  {
    def gridRoordTrans(focus: Roord, scale: Double): T = value.trans(orig => (orig - focus.gridVec2) * scale)
    def gridRoordTrans(yFocus: Int, cFocus: Int, scale: Double): T = value.trans(orig => (orig - grid.roordToVec2(yFocus, cFocus)) * scale)
  }
  
  implicit class GridSlateScaleExtension[T](value: T)(implicit grid: TileGridSimple, evSlate: Slate[T], evScale: Scale[T]) {
    /** Translates Vec2s relative to Grid centre and then scales. */
    def gridScale(scale: Double): T =
    { val a = evSlate.slateT(value, - grid.cen)
      evScale.scaleT(a, scale)
    }
    /** Translates Vec2s relative to focus and then scales. */
    def gridRoordScale(focus: Roord, scale: Double): T =
    { val a = evSlate.slateT(value, - focus.gridVec2)
      evScale.scaleT(a, scale)
    }
  }

  implicit class GridTransSimExtension[T](value: T)(implicit grid: TileGridSimple, ev: TransSim[T])
  {
    def gridTrans(offset: Vec2, scale: Double): T =
    { val a = ev.slate(value, -offset - grid.cen)
      ev.scale(a, scale)
    }
  }

  /** Not sure about the use of List in this class. */
  implicit class TilesListImplicit[A](thisRefs: TilesArr[List[A]])
  { def prepend(y: Int, c: Int, value: A)(implicit grid: TileGrid): Unit = prepend(Roord(y, c), value)
    def prepend(roord: Roord, value: A)(implicit grid: TileGrid): Unit = thisRefs.unsafeArr(grid.arrIndex(roord)) ::= value
    def prepends(value : A, roords: Roord*)(implicit grid: TileGrid): Unit = roords.foreach{ r =>  thisRefs.unsafeArr(grid.arrIndex(r)) ::= value }
  }

  val htStepSomes: Arr[HTStep] = Arr(HTStepUR, HTStepRt, HTStepDR, HTStepDL, HTStepLt, HTStepUL)
  val htSteps: Arr[HTStepLike] = HTStepNone +: htStepSomes
}