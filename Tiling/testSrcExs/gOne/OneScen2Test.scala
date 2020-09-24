/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package gOne
import utest._

object OneScen2Test  extends TestSuite
{
  val g = OneScen2.grid
  val tests = Tests
  {
    "test1" -
    { g.numOfTileRows ==> 4
      g.yTileMin ==> 2
      g.yTileMax ==> 8
      g.cTileMin ==> 2
      g.cTileMax ==> 10
      g.cRowStart(2) ==> 2
      g.cRowLen(2) ==> 3
      g.cRowEnd(2) ==> 10
      g.cRowLen(4) ==> 2
      g.cRowLen(6) ==> 1
      g.cRowEnd(6) ==> 6
      g.cRowLen(8) ==> 2
      g.cRowEnd(8) ==> 8
      g.numOfTiles ==> 8
    }

    "Sides" -
    {
      g.sideRoords.length ==> 36
    }
   }
}