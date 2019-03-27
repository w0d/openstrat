/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pGrid
import geom._, reflect.ClassTag

class HexGridReg[TileT <: Tile, SideT <: TileSide](xTileMin: Int, xTileMax: Int, yTileMin: Int, yTileMax: Int)(implicit evTile: ClassTag[TileT],
    evSide: ClassTag[SideT]) extends HexGrid[TileT, SideT](xTileMin, xTileMax, yTileMin, yTileMax) with TileGridReg[TileT, SideT]
{
  override def coodToVec2(cood: Cood): Vec2 = HexGrid.coodToVec2(cood)
  def vertMargin = 2.8
  def horrMargin = 2.2  
  final def left: Double = xTileMin - horrMargin
  final def right: Double = xTileMax + horrMargin
  final def bottom: Double = yTileMin  * yRatio - vertMargin
  final def top: Double = yTileMax * yRatio + vertMargin  
  
  override def rowTileXStart(y: Int): Int = ife(y %% 4 == 0, xRow4Start, xRow2Start)
  override def rowTileXEnd(y: Int): Int = ife(y %% 4 == 0, xRow4End, xRow2End)
  def xRow2Start: Int = xTileMin.incrementTill(_ % 4 == 2)
  def xRow4Start: Int = xTileMin.incrementTill(_ % 4 == 0)
  def xRow2End: Int = xTileMax.decrementTill(_ % 4 == 2)
  def xRow4End: Int = xTileMax.decrementTill(_ % 4 == 0)
  def xRow2Len: Int = ((xRow2End - xRow2Start) / 2 + 1).max(0)
  def xRow4Len: Int = ((xRow4End - xRow4Start) / 2 + 1).max(0)
  def yRow2Start: Int = yTileMin.incrementTill(_ % 4 == 2)
  def yRow4Start: Int = yTileMin.incrementTill(_ % 4 == 0)
  def yRow2End: Int = yTileMax.decrementTill(_ % 4 == 2)
  def yRow4End: Int = yTileMax.decrementTill(_ % 4 == 0)
  def yRow2Len: Int = ((yRow2End - yRow2Start) / 2 + 1).max(0)
  def yRow4Len: Int = ((yRow2End - yRow2Start) / 2 + 1).max(0)
  
  /** Not sure about the following 4 values */
  def sideRow2Start = xRow2Start - 2
  def sideRow4Start = xRow4Start - 2
  def sideRow2End = xRow2End + 2
  def sideRow4End = xRow4End + 2
  def sideRowOddStart = xRow2Start.max(xRow4Start) - 1 
  def sideRowOddEnd = xRow2End.max(xRow4End) + 1
  def sideRowOddLen: Int = (sideRowOddEnd - sideRowOddStart) / 2
  override def tileNum: Int = xRow2Len * yRow2Len + xRow4Len * yRow4Len
  override def sideNum: Int = ???
  
  val sideArr: Array[SideT] = new Array[SideT](sideArrLen)
  
  /** rows 2, 6, 10 ... -2, -6, -10 ... */
  def row2sForeach(f: Int => Unit): Unit = for { y <- yRow2Start to yRow2End by 4 } yield f(y)
      
  /** rows 4, 8 12 ... 0, -4, -8 ... */
  def row4sForeach(f: Int => Unit): Unit =
    for { y <- yRow4Start to yRow4End by 4 } yield f(y)      
  
  /** Needs more work. */
  final override def foreachSidesXYAll(f: (Int, Int) => Unit): Unit = 
  {   
    if (tileNum == 0) return    
    rowForeachTilesXYAll(yTileMin, (x, y) => { f(x - 1, y - 1); f(x + 1, y - 1) })    
    for { y <- (yTileMin + 1) to (yTileMax - 1) by 2
      x <- sideRowOddStart to sideRowOddEnd by 2
    } f(x, y)
    foreachTilesXYAll{ (x, y) => f(x + 2, y)}
    rowForeachTilesXYAll(yTileMax, (x, y) => { f(x - 1, y + 1); f(x + 1, y + 1) })    
  }
   
  def tileNeighboursCoods(cood: Cood): Coods =
    HexGrid.adjTileCoodsOfTile(cood).filter(c => yTileMax >= c.y & c.y >= yTileMin & xTileMax >= c.x & c.x >= xTileMin)
  def tileNeighbours(tile: TileT): List[TileT] = tileNeighboursCoods(tile.cood).lMap(getTile)  
     
  def findPath(startCood: Cood, endCood: Cood, fTerrCost: (TileT, TileT) => OptInt): Option[List[Cood]] =
  {
    var open: List[Node[TileT]] = Node(this.getTile(startCood), 0, getHCost(startCood, endCood), nullRef[Node[TileT]]) :: Nil
    var closed: List[Node[TileT]] = Nil
    var found: Option[Node[TileT]] = None
    while (open.nonEmpty & found == None)
    {
      val curr: Node[TileT] = open.minBy(_.fCost)
      //if (curr.tile.cood == endCood) found = true
      open = open.filterNot(_ == curr)
      closed ::= curr
      val neighbs: List[TileT] = this.tileNeighbours(curr.tile).filterNot(tile => closed.exists(_.tile == tile))
      neighbs.foreach { tile =>
        fTerrCost(curr.tile, tile) match
        {
          case NoInt =>
          case SomeInt(nc) if closed.exists(_.tile == tile) =>
          case SomeInt(nc) =>
          {
            val newGCost = nc + curr.gCost
            
            open.find(_.tile == tile) match
            {
              case Some(node) if newGCost < node.gCost => { node.gCost = newGCost; node.parent = Opt(curr) }
              case Some(node) =>
              case None => 
              {
                val newNode  = Node(tile, newGCost, getHCost(tile.cood, endCood), Opt(curr))
                open ::= newNode
                if (tile.cood == endCood) found = Some(newNode)
              }
            }
          }
        }
      }       
   }
     
  def loop(acc: List[Cood], curr: Node[TileT]): List[Cood] = curr.parent.fold(acc, loop(curr.tile.cood :: acc, _))
   
  found.map(endNode =>  loop(Nil, endNode))
  }
  
  /* ****************************** SideStuff ****************************************/
  //override def allSideCoods: Coods = ???
  final override def setTiles[A](xFrom: Int, xTo: Int, yFrom: Int, yTo: Int, tileValue: A)(implicit f: (Int, Int, A) => TileT): Unit = ???
}

case class Node[TileT <: Tile](val tile: TileT, var gCost: Int, var hCost: Int, var parent: Opt[Node[TileT]])
{
  def fCost = gCost + hCost
}