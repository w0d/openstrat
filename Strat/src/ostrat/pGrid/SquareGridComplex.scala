/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pGrid

/** This represents a non-Simple square grid where the tile sides can have their own values. So for square the classic example is walls. 
 *  The wall is too thin to occupy a whole tile or a line of tiles. For the time being all square grids are presumed to be regular grids */
abstract class SquareGridComplex[TileT <: Tile, SideT <: GridElem](val xTileMin: Int, val xTileMax: Int, val yTileMin: Int, val yTileMax: Int)
  (implicit val evTile: IsType[TileT], val evSide: IsType[SideT]) extends TileGridComplexReg[TileT, SideT] with SquareGrid[TileT]
{  
  override def xToInd(x: Int): Int = (x - xTileMin + 1)
  override def yToInd(y: Int): Int = (y  - yTileMin + 1)
  override def xArrLen: Int = xTileMax - xTileMin + 3
  override val yArrLen: Int = yTileMax - yTileMin + 3//+ 1 for lowersides +1 for zeroth tile, + 1 for upper side(s)
  override val arr: Array[AnyRef] = new Array[AnyRef](arrLen)
  override def vertCoodsOfTile(tileCood: Cood): Coods = SquareGridComplex.vertCoodsOfTile(tileCood)
  override def sideCoodsOfTile(tileCood: Cood): Coods = SquareGridComplex.sideCoodsOfTile(tileCood)   
  
  override def xStep: Int = 2
   
  def left: Double = xTileMin - 1.1
  def right: Double = xTileMax + 1.1
  def bottom: Double = yTileMin - 1.1
  def top: Double = yTileMax + 1.1 
 
  
  override def coodIsTile(x: Int, y: Int): Unit = Unit match
  { case _ if x %% 2 == 0 & y %% 2 == 0 =>      
    case _ => excep(x.toString.commaAppend(y.toString) -- "is an invalid Square tile coordinate") 
  }
  
  override def coodIsSide(x: Int, y: Int): Unit = Unit match
  { case _ if x.isOdd & y.isOdd =>   
    case _ => excep(x.toString.commaAppend(y.toString) -- "is an invalid Squareside tile coordinate")   
  }
  
  def tileXYForeach(f: (Int, Int) => Unit): Unit = for { y <- yTileMin to yTileMax by 2
    x <- xTileMin to xTileMax by 2      
  } f(x, y)
   
  def sideXYForeach(f: (Int, Int) => Unit): Unit =
  {
    for {y <- yTileMin to yTileMax by 2
      x <- xTileMin.plus1 to xTileMax.minus1 by 2
    } f(x, y)
    for {y <- yTileMin.plus1 to yTileMax.minus1 by 2
      x <- xTileMin to xTileMax by 2
    } f(x, y)
  }
   
  /** Sets a rectangle of tiles to the same terrain type. */
  final def setRectangle[A](bottomLeft: Cood, topRight: Cood, tileMaker: A)(implicit f: (Int, Int, A) => TileT): Unit = for {
    y <- bottomLeft.y to topRight.y by 2
    x <- bottomLeft.x to topRight.x by 2
  } setTile(x, y, f(x, y, tileMaker))
   
   
  final def setColumn[A](x: Int, yStart: Int, tileMakers: Multiple[A]*)(implicit f: (Int, Int, A) => TileT) : Cood =
  {
    val tiles = tileMakers.flatMap(_.toSeq)      
    tiles.iForeach{(e, i) =>
      val y = yStart + i * 2
      setTile(x, y, f(x, y , e))      
    }
    Cood(x, yStart + (tiles.length - 1) * 2)
  }
  
  final def setColumn[A](cood: Cood, tileMakers: Multiple[A]*)(implicit f: (Int, Int, A) => TileT): Cood =
    setColumn(cood.x, cood.y, tileMakers: _*)(f)
   
  final def setColumnDown[A](x: Int, yStart: Int, tileMakers: Multiple[A]*)(implicit f: (Int, Int, A) => TileT) : Cood =
  {
    val tiles = tileMakers.flatMap(_.toSeq)
      
    tiles.iForeach{(e, i) =>
      val y = yStart - i * 2
      setTile(x, y, f(x, y, e))               
    }
    Cood(x, yStart - (tiles.length - 1) * 2)
  }
  
  final def setColumnDown[A](cood: Cood, tileMakers: Multiple[A]*)(implicit f: (Int, Int, A) => TileT) : Cood =
    setColumnDown(cood.x, cood.y, tileMakers: _*)(f)
   
  def fSetRow[A](f: A => TileT, y: Int, tileMakers: Multiple[A]*): Unit =
  {
    val tiles = tileMakers.flatMap(_.toSeq)     
    tiles.iForeach((e, i) => setTile(xTileMin + i * 2, y, f(e)))
  }
   
  def setTerrPath[A](value: A, startCood: Cood, dirns: Multiple[SquareGridComplex.PathDirn]*)(implicit f: (Int, Int, A) => TileT): Unit =
  {
    var cood = Cood(startCood.x, startCood.y)
    import SquareGridComplex._
    
    dirns.foreach 
    { case Multiple(Rt, i) => cood = setRow(cood, value * i)(f)
      case Multiple(Lt, i) => cood = setRowBack(cood, value * i)(f)
      case Multiple(Up, i) => cood = setColumn(cood, value * i)(f)
      case Multiple(Dn, i) => cood = setColumnDown(cood, value * i)(f)
    }
    cood
  }
  
  override def vertCoodLineOfSide(x: Int, y: Int): CoodLine = SquareGridComplex.vertCoodLineOfSide(x, y)
  
  override def sidesTileCoods(x: Int, y: Int): (Cood, Cood) = Unit match
  {
    case _ if x.isOdd & y.isEven => (Cood(x - 1, y), Cood(x + 1, y))
    case _ if x.isEven & y.isOdd => (Cood(x, y - 1), Cood(x, y + 1))
    case _ => excep("Invalid Square Coordinate")
  }
 
  /** Warning needs Modification */
  override def adjTileCoodsOfTile(tileCood: Cood): Coods = SquareGridComplex.adjTileCoodsOfTile(tileCood)
}

object SquareGridComplex
{
  val vertCoodsOfTile00: Coods = Coods.xy(1,1,  1,-1,  -1,-1, -1,1)
  def vertCoodsOfTile(inp: Cood): Coods = vertCoodsOfTile00.pMap(inp + _)
  val adjTileCoodsOfTile00: Coods = Coods(0 cc 2, 2 cc 2, 2 cc 0, 2 cc -2, 0 cc -2, -2 cc -2, -2 cc 0)
  def adjTileCoodsOfTile(tileCood: Cood): Coods = adjTileCoodsOfTile00.pMap(tileCood + _)
  def sideCoodsOfTile(inp: Cood): Coods = ???
   
  sealed trait PathDirn
  object Rt extends PathDirn
  object Lt extends PathDirn
  object Up extends PathDirn
  object Dn extends PathDirn
   
  def vertCoodLineOfSide(sideCood: Cood): CoodLine = vertCoodLineOfSide(sideCood.x, sideCood.y)
  
  def vertCoodLineOfSide(x: Int, y: Int): CoodLine = (x %% 2, y %% 2) match
  { case (1, 0) => CoodLine(x, y + 1, x, y - 1)
    case (0, 1)=> CoodLine(x - 1, y, x + 1, y)
    case _ => excep("Invalid Square Cood for a side")
  } 
}