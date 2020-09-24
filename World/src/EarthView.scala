/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pEarth
import geom._

/** A view of the Earth. Currently North can only be up or down. */
case class EarthView (latLong: LatLong, scale: Dist, up: Boolean)

object EarthView
{
  /** Not sure about the scale .metres parameter conversion */
  implicit object EarthViewPersist extends Persist3[LatLong, Double, Boolean, EarthView]("EarthView", "latLong", _.latLong,
    "scale", _.scale.metres / 1000, "up", _.up, (ll: LatLong, d: Double, b: Boolean) => EarthView(ll, Dist(d * 1000), b))
}