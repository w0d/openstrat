/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package pEarth
import geom._, pGrid._, reflect.ClassTag

/** Not sure whether the "fTile: (Int, Int, Terrain) => TileT" should be implicit. Will change with multiple implicit parameter lists */
trait EGridMaker
{
  def apply[TileT <: TileOld, SideT <: TileSideOld]
    (implicit fTile: (Int, Int, WTile) => TileT, fSide: (Int, Int, SideTerr) => SideT, evTile: ClassTag[TileT], evSide: ClassTag[SideT]):
    EGridOld[TileT, SideT]
}

/** A Hex Grid for an area of the earth. It is irregular because as you move towards the poles the row length decreases. The x dirn 
 *  follows lines of longitude. The y Axis at the cenLong moves along a line of longitude. */
class EGridOld[TileT <: TileOld, SideT <: TileSideOld](bounds: Array[Int], val name: String, val cenLong: Longitude, val scale: Dist, val xOffset: Int,
  val yOffset: Int, xTileMin: Int, xTileMax: Int, yTileMin: Int, yTileMax: Int, turnNum: Int)(implicit evTile: ClassTag[TileT],
  evSide: ClassTag[SideT]) extends HexGridOldIrr[TileT, SideT](bounds, xTileMin, xTileMax, yTileMin, yTileMax, turnNum)
{
  thisEGrid =>
  type GridT <: EGridOld[TileT, SideT]
  val vec2ToLL: Vec2 => LatLong = fVec2ToLatLongReg(cenLong, scale, xOffset, yOffset)
  def vToLL(vIn: Vec2) : LatLong = vec2ToLL(vIn)

  override def toString: String = "Grid " + name

  def vertLL(x: Int, y: Int, ptNum: Int): LatLong = ???

  def llXLen =  (xTileMax - xTileMin + 5) * 2
  def llYLen = yTileMax - yTileMin + 3
  val vArr: Array[Double] = new Array[Double](llYLen * llXLen)
  def llXInd(x: Int): Int = (x - xTileMin + 2) * 2
  def llYInd(y: Int): Int = (y - yTileMin + 1) * llXLen
  def llInd(x: Int, y: Int): Int = llYInd(y) + llXInd(x)

  def getLL(x: Int, y: Int): LatLong =
  { val index: Int = llInd(x, y)
    LatLong.radians(vArr(index), vArr(index + 1))
  }

  def getLL(cood: Cood): LatLong = getLL(cood.xi ,cood.yi)

  def setLL(x: Int, y: Int, llValue: LatLong): Unit =
  { val index: Int = llInd(x, y)
    vArr(index) = llValue.latRadians
    vArr(index + 1) = llValue.longRadians
  }

  def setLL(cood: Cood, llValue: LatLong): Unit = setLL(cood.xi, cood.yi, llValue)
  final def setLongitude(cood: Cood, radians: Double): Unit = setLongitude(cood.xi, cood.yi, radians)
  def setLongitude(x: Int, y: Int, radians: Double): Unit = vArr(llInd(x, y) + 1) = radians
  /** These 2 methods may be redundant */
  def coodToLL(cood: Cood): LatLong = coodToLL(cood.xi, cood.yi) //vec2ToLatLongReg(HG.coodToVec2(hc), cenLong, scale, xOffset, yOffset)
  def coodToLL(x: Int, y: Int): LatLong = getLL(x, y)//vec2ToLL(coodToVec2(cood))

  foreachTilesCoodAll{cood =>
    setLL(cood, vec2ToLL(HexGridOld.coodToVec2(cood)))
    sideCoodsOfTile(cood).foreach(vc => setLL(vc, vec2ToLL(HexGridOld.coodToVec2(vc))))
    vertCoodsOfTile(cood).foreach(vc => setLL(vc, vec2ToLL(HexGridOld.coodToVec2(vc))))
  }

  def ofETilesFold[R](eg: EarthGuiOld, f: OfETile[TileT, SideT] => R, fSum: (R, R) => R)(emptyVal: R) =
  {
    var acc: R = emptyVal
    foreachTilesCoodAll{ tileCood =>
      val newRes: R = f(new OfETile[TileT, SideT](eg, thisEGrid ,getTile(tileCood)))
      acc = fSum(acc, newRes)
    }
    acc
  }

  def eGraphicElems(eg: EarthGuiOld, fDisp: (OfETile[TileT, SideT]) => DisplayElems, sDisp: (OfESide[TileT, SideT]) => DisplayElems):
    DisplayElems =
  {
    val acc: Buff[GraphicElem] = Buff()
    foreachTilesCoodAll { tileCood =>
      val tog = new OfETile[TileT, SideT](eg, thisEGrid, getTile(tileCood))
      val newRes: DisplayElems = ife(tog.cenFacing, fDisp(tog), Arr[GraphicAffineElem]())
      acc ++= newRes.unsafeArr
    }

    val sideAcc: Buff[GraphicElem] = Buff()
    foreachSidesCoodAll { sideCood =>
      val tog = new OfESide[TileT, SideT](eg, thisEGrid, getSide(sideCood))
      val newRes: DisplayElems = ife(tog.sideCenFacing, sDisp(tog), Arr[GraphicAffineElem]())
      sideAcc ++= newRes.unsafeArr
    }
    (acc ++ sideAcc).toRefs
  }

  var rightGrid: Option[EGridOld[TileT, SideT]] = None
}