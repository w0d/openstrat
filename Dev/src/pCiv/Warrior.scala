/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package pCiv

case class Warrior(faction: Faction)
{ def colour: Colour = faction.colour
}