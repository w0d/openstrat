package ostrat
package pGames.pChess
import geom._, pGrid._, Colour._

sealed trait Piece
object Pawn extends Piece
object Rook extends Piece
{
  def rtLine: Vec2s = Vec2s(0 vv 100, 5 vv 100, 30 vv 100, 30 vv 80, 20 vv 80, 30 vv 0, 0 vv 0)
}
object Knight extends Piece
object Bishop extends Piece
object Queen extends Piece
object King extends Piece

sealed trait Player extends WithColour

object PWhite extends Player
{ def colour = White
}

object PBlack extends Player
{ def colour = Black
}

/** Player Piece */
case class PPiece(player: Player, piece: Piece)

class ChessGrid (val array: Array[Option[PPiece]]) extends AnyVal with SqSqArr[Option[PPiece]]
{ type GridT = ChessGrid
  override def thisFac = new ChessGrid(_)
  def size = 8
}