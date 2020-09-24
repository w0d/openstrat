/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pEarth
import geom._

/** A second level area */
abstract class Area2(val symName: String, val cen: LatLong, val terr: WTile) extends GeographicSymbolKey
{
   override def toString = name.appendCommas(terr.toString)
   def aStrs: Arr[String] = Arr(name)
   def textScale: Dist = 15.km   
   def latLongs: LatLongs   
   
   def display(eg: EarthGuiOld, fill: Boolean = true): DisplayElems =
   {  
      eg.polyToGlobedArea(latLongs) match
      {
         case GlobedAll(d2s) =>
         { 
            val v2s: PolygonGen = d2s.pMap(eg.trans)
            val cenXY: Vec2 = eg.latLongToXY(cen)
            val vis1: DisplayElems = ife(fill, Arr(v2s.fillActive(terr.colour, this)), Arr())
            val vis2: DisplayElems = Arr(v2s.draw(2.0, terr.colour.redOrPink))
            val vis3: DisplayElems =
              if (eg.scale < textScale && fill) TextGraphic.lines(aStrs, 10, cenXY, terr.contrast)
              else Arr()
            (vis1 ++ vis2 ++ vis3)
         }
         case GlobedSome(curveSegDists) =>
         {
            val cenXY: Vec2 = eg.latLongToXY(cen)
            val curveSegs: PolyCurve = curveSegDists.pMap(_.toCurveSeg(eg.trans))
            Arr(PolyCurveParentFull.fill(cenXY, curveSegs, this, terr.colour))
         }
         case GlobedNone => Arr()
      }
   }   
}

object Area2
{
   def apply(symName: String, cen: LatLong, terr: WTile, latLongArgs: LatLong*) = new Area2(symName, cen, terr)
   {      
      val latLongs = LatLongs(latLongArgs: _*)
   }  
}