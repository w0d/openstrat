/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pGrid
import geom._

trait EGrid extends HexGridIrr
{
  def roodToLL(roord: Roord): LatLong
}
