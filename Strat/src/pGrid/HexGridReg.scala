package ostrat
package pGrid

case class HexGridReg(xTileMin: Int, xTileMax: Int, yTileMin: Int, yTileMax: Int) extends HexGrid
{
  /** Minimum x for Rows where y.Div4Rem2. */
  def xRow2sMin: Int = xTileMax.roundUpTo(_.div4Rem2)

  /** Maximum x for Rows where y.Div4Rem2. */
  def xRow2sMax: Int = xTileMax.roundDownTo(_.div4Rem2)

  def row2sTileLen = ((xRow2sMax - xRow2sMin + 4) / 4).min0

  /** Minimum x for Rows where y.Div4Rem0. */
  def xRow0sMin: Int = xTileMax.roundUpTo(_.div4Rem0)

  /** Maximum x for Rows where y.Div4Rem0. */
  def xRow0sMax: Int = xTileMax.roundDownTo(_.div4Rem0)

  def row0sTileLen = ((xRow0sMax - xRow0sMin + 4) / 4).min0

  def yRow2sMin: Int = yTileMin.roundUpTo(_.div4Rem2)
  def yRow2sMax: Int = yTileMax.roundDownTo(_.div4Rem2)
  /** Number of Rows where y.Div4Rem2. */
  def numOfRow2s: Int = ((yRow2sMax - yRow2sMin + 4) / 4).min0

  def yRow0sMin: Int = yTileMin.roundUpTo(_.div4Rem0)
  def yRow0sMax: Int = yTileMax.roundDownTo(_.div4Rem0)

  /** Number of Rows where y.Div4Rem0. */
  def numOfRow0s: Int = ((yRow0sMax - yRow0sMin + 4) / 4).min0

  override def numOfTiles: Int = numOfRow2s * row2sTileLen + numOfRow0s * row0sTileLen

  override def allTilesForeach(f: Cood => Unit): Unit =
  { ijToForeach(yRow2sMin, yRow2sMax, 4)(xRow2sMin, xRow2sMax, 4)((y, x) => f(Cood(x, y)))
    ijToForeach(yRow0sMin, yRow0sMax, 4)(xRow0sMin, xRow0sMax, 4)((y, x) => f(Cood(x, y)))
  }
}