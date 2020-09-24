/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package gOne
import pCanv._, geom._, pGrid._

/** Graphical user interface for GOne example game. */
case class GOneGui(canv: CanvasPlatform, scenStart: OneScen) extends CmdBarGui("Game One Gui")
{ var scen = scenStart
  var statusText = "Let click on Player to select. Right click on adjacent Hex to set move."
  implicit def grid = scen.grid
  def players: TilesArrOpt[Player] = scen.oPlayers

  /** There are mo moves set. The Gui is reset to this state at the start of every turn. */
  val NoMoves: TilesArrOpt[HTileAndStep] = grid.newTileArrOpt[HTileAndStep]

  /** This is the planned moves or orders for the next turn. Note this is just a record of the planned moves it is not graphical display of
   *  those moves. This data is state for the Gui. */
  var moves: TilesArrOpt[HTileAndStep] = NoMoves

  /** The number of pixels / 2 displayed per row height. */
  val scale = grid.fullDisplayScale(mainWidth, mainHeight)

  def lunits = players.mapSomes{(r, p) => Rect.applyOld(0.9, 0.6, r.gridVec2).fillDrawTextActive(p.colour, RPlayer(p, r),
    p.toString + "\n" + r.ycStr, 24, 2.0) }

  /** This makes the tiles active. They repsond to mouse clicks. It does not paint or draw the tiles. */
  val tiles = grid.activeTiles

  /** Gives the tiles Roord. Its Row based integer coordinate. */
  val roardTexts = grid.cenRoordIndexTexts() ++ grid.sideRoordIndexTexts() ++ grid.vertRoordIndexTexts()

  /** Draws the tiles sides (or edges). */
  val sidesDraw = grid.sidesDraw(2.0)

  /** This is the graphical display of the planned move orders. */
  def moveGraphics: Arr[LineDraw] = moves.mapSomeOnlys{ rs => RoordLine(rs.r1, rs.r2).gridLine2.draw(2, players(rs.r1).colour ) }

  /** Creates the turn button and the action to commit on mouse click. */
  def bTurn = clickButton("Turn " + (scen.turn + 1).toString, _ => {
    val getOrders = moves.mapSomeOnlys(rs => rs)
    scen = scen.turn(getOrders)
    moves = NoMoves
    repaint()
    thisTop()
  })

  /** The frame to refresh the top command bar. Note it is a ref so will change with scenario state. */
  def thisTop(): Unit = reTop(Arr(bTurn, status))

  mainMouseUp = (b, cl, _) => (b, cl, selected) match
    { case (LeftButton, cl, _) =>
      { selected = cl
        statusText = selected.headToStringElse("Nothing Selected")
        thisTop()
      }

      case (RightButton, (t : HexTile) :: _, List(RPlayer(p, r), HexTile(y, c))) =>
      {
        val newM: OptRef[HTStep] = t.adjOf(r)
        newM.foreach(m => moves = moves.setSome(r, r.andStep(m)))//grid.index(r), m))
        repaint()
      }
       case (_, h, _) => deb("Other; " + h.toString)
    }
  thisTop()
  def frame = (tiles +- sidesDraw ++ roardTexts ++ lunits ++ moveGraphics).gridScale(scale)
  def repaint() = mainRepaint(frame)
  repaint()
}