/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pEarth
import pGrid._

trait ETileOld extends ColouredTileOld
{ def terr: WTile
  def colour: Colour = terr.colour
  def str: String = terr.str
}



