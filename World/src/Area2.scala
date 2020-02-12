/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pEarth
import geom._

/** A second level area */
abstract class Area2(val symName: String, val cen: LatLong, val terr: WTile) extends GeographicSymbolKey
{
   override def toString = name.appendCommas(terr.toString)
   def aStrs = ArrOld(name)
   def textScale: Dist = 15.km   
   def latLongs: LatLongs   
   
   def display(eg: EarthGui, fill: Boolean = true): GraphicElemsOld =
   {  
      eg.polyToGlobedArea(latLongs) match
      {
         case GlobedAll(d2s) =>
         { 
            val v2s: Polygon = d2s.pMap(eg.trans)// eg.transSeq(d2s)
            val cenXY: Vec2 = eg.latLongToXY(cen)
            val vis1: GraphicElems = ife(fill, v2s.fillActive(terr.colour, this), Refs())
            val vis2: GraphicElems = Refs(v2s.draw(2.0, terr.colour.redOrPink))
            val vis3: GraphicElems =
              if (eg.scale < textScale && fill) TextGraphic.lines(aStrs.toRefs, 10, cenXY, terr.contrast)
              else Refs()
            (vis1 ++ vis2 ++ vis3).toArraySeq
         }
         case GlobedSome(curveSegDists) =>
         {
            val cenXY: Vec2 = eg.latLongToXY(cen)
            val curveSegs: Shape = curveSegDists.pMap(_.toCurveSeg(eg.trans))
            ArrOld(ShapeSubj.fill(cenXY, curveSegs, this, terr.colour))
         }
         case GlobedNone => ArrOld()
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