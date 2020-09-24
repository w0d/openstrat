/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
import geom._, pGrid._

/** This package and module is for Earth maps. In particular the tiling of the whole world in Hex grids, defining the changes over the course of
 *  history. This will be a data orientated module. It will also include terrain types to model terrain, both real and imagined for local maps and
 *  higher scales right up to 0.5 metres per tile However it won't generally include the data for these. The data for the real world will be organised
 *  according to a number of levels, which are likely to change over increasingly shorter historical time frames.
 *
 *  1 Base elevation, relative to 1950 sea level, and relief.
 *  2 Climate.
 *  2 Sea level, shore lines, lake shore lines and river courses.
 *  3 Land-use, both natural and human. */
package object pEarth
{
   /** The North-South divide between Area1s and Grids at 45 degrees north approx. */
   val divN45 = 45.27369792435918.north
   //import HexGrid._
   /** Returns a function for a specific EGrid to convert from gridVec to Latlong */
   def fVec2ToLatLongReg(refLong: Longitude, scale: Dist, xOffset: Int, yOffset: Int = 0): Vec2 => LatLong = inp =>
      {
         val vOffset: Vec2 = HexGridOld.coodToVec2(xOffset, yOffset)
         val d2: Dist2 = (inp - vOffset) * scale
         val lat: Double = d2.y / EarthPolarRadius         
         val longDelta: Double =   d2.x / (EarthEquatorialRadius * math.cos(lat))
         LatLong.radians(lat, refLong.radians + longDelta)
      }
      
   def vec2ToLatLongReg(inp: Vec2, refLong: Longitude, scale: Dist, xOffset: Int, yOffset: Int = 0): LatLong =
      {
         val vOffset: Vec2 = HexGridOld.coodToVec2(xOffset, yOffset)
         val d2: Dist2 = (inp - vOffset) * scale
         val lat: Double = d2.y / EarthPolarRadius         
         val longDelta: Double =   d2.x / (EarthEquatorialRadius * math.cos(lat))
         LatLong.radians(lat, refLong.radians + longDelta)
      }   
   
   /** Not necessarily used */   
   def vec2ToLatLong0(inp: Vec2, refLong: Longitude, scale: Dist, yOffset: Int = 0): LatLong =
   {
      val vOffset: Vec2 = HexGridOld.coodToVec2(0, yOffset)
      val d2: Dist2 = (inp - vOffset) * scale
      val lat: Double = d2.y / EarthPolarRadius         
      val longDelta: Double =   d2.x / (EarthEquatorialRadius * math.cos(lat))
      LatLong.radians(lat, refLong.radians + longDelta)
   }
   
   /** Not necessarily used */
   def  coodToLatLong0(inp: Cood, scale: Dist, yOffset: Int = 0): LatLong =
   {
      val adj: Vec2 = HexGridOld.coodToVec2(inp.subY(yOffset))
      val d2: Dist2 = adj * scale
      val lat = d2.y / EarthPolarRadius         
      val longDelta: Double =   d2.x / (EarthEquatorialRadius * math.cos(lat))
      LatLong.radians(lat, longDelta)
   } 
}