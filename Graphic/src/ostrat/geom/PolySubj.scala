/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package geom
import Colour.Black

case class PolySubj(cen: Vec2, poly: Polygon, evObj: AnyRef, elems: List[PaintElem[_]], zOrder: Int = 0) extends GraphicSubject[PolySubj] with
PolyActive
{  
   def fTrans(f: Vec2 => Vec2): PolySubj = new PolySubj(f(cen), poly.fTrans(f), evObj, elems.fTrans(f), zOrder)   
   override def addElems(newElems: List[PaintElem[_]]): PolySubj = new PolySubj(cen, poly, evObj, elems ::: newElems, zOrder)
   override def mutObj(newObj: AnyRef): PolySubj = new PolySubj(cen, poly, newObj, elems, zOrder)
}

object PolySubj
{
  def fill(cen: Vec2, poly: Polygon, evObj: AnyRef, colour: Colour, zOrder: Int = 0) = new PolySubj(cen, poly, evObj, List(poly.fill(colour)),
      zOrder)
   
  /** Not sure if this is double filling the polygon */
  def fillDraw(cen: Vec2, poly: Polygon, evObj: AnyRef, fillColour: Colour, lineWidth: Double, lineColour: Colour = Black, zOrder: Int = 0) =
    new PolySubj(cen, poly, evObj, List(PolyFillDraw(poly, fillColour, lineWidth, lineColour, zOrder)), zOrder)
   
  def draw(cen: Vec2, poly: Polygon, evObj: AnyRef, lineWidth: Double, lineColour: Colour = Black) =
      new PolySubj(cen, poly, evObj, List(PolyDraw(poly, lineWidth, lineColour)))
  
  def fillText(cen: Vec2, poly: Polygon, evObj: AnyRef, fillColour: Colour, str: String, fontSize: Int = 4, fontColour: Colour = Colour.Black,
      align: TextAlign = TextCen) =
        new PolySubj(cen, poly, evObj, List(poly.fill(fillColour), TextGraphic(str, fontSize, poly.polyCentre, fontColour, align)))
  
  def fillContrastText(cen: Vec2, poly: Polygon, evObj: AnyRef, fillColour: Colour, str: String, fontSize: Int = 4) =
    fillText(cen, poly, evObj, fillColour, str, fontSize, fillColour.contrast)
}
