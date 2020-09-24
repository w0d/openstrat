/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package pDung
import Colour._, pGrid._

sealed class Faction(val str: String, val colour: Colour) extends PersistSingleton
{// def typeSym = 'Faction
}

object Fac1 extends Faction("Fac1", Orange)
object Fac2 extends Faction("Fac2", Green)

class Character(val iden: Char, val faction: Faction)// extends CoodMover with PersistSingleton
{ //def typeSym = 'Character
  def colour = faction.colour
  var facing: SqFace = SFaceUp
  val str: String = "Character" -- iden.toString
 // def canMove(tile: DTileOld): Boolean = tile.terr != Wall
  def turnMovePts = 10
  var movePts: Int = turnMovePts
  def resetMovePts(): Unit = movePts = turnMovePts
}

object CharacA extends Character('A', Fac1)
object CharacB extends Character('B', Fac1)

object CharacY extends Character('Y', Fac2)
object CharacZ extends Character('Z', Fac2)