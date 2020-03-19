/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package geom
import Colour.Black

case class PolyParent(cen: Vec2, poly: Polygon, pointerId: Any, elems: Refs[PaintElem]) extends GraphicParent with PolyActive
{ def fTrans(f: Vec2 => Vec2): PolyParent = new PolyParent(f(cen), poly.fTrans(f), pointerId, elems.trans(f))
  override def addElems(newElems: Refs[PaintElem]): PolyParent = new PolyParent(cen, poly, pointerId, elems ++ newElems)
  override def mutObj(newObj: Any): PolyParent = new PolyParent(cen, poly, newObj, elems)
}

object PolyParent
{
  def fill(cen: Vec2, poly: Polygon, evObj: Any, colour: Colour): PolyParent = new PolyParent(cen, poly, evObj, Refs(poly.fill(colour)))

  /** Not sure if this is double filling the polygon */
  def fillDraw(cen: Vec2, poly: Polygon, evObj: Any, fillColour: Colour, lineWidth: Double, lineColour: Colour = Black): PolyParent =
    new PolyParent(cen, poly, evObj, Refs(PolyFillDraw(poly, fillColour, lineWidth, lineColour)))

  def draw(cen: Vec2, poly: Polygon, evObj: Any, lineWidth: Double, lineColour: Colour = Black): PolyParent =
    new PolyParent(cen, poly, evObj, Refs(PolyDraw(poly, lineWidth, lineColour)))

  def fillText(cen: Vec2, poly: Polygon, evObj: Any, fillColour: Colour, str: String, fontSize: Int = 4, fontColour: Colour = Colour.Black,
    align: TextAlign = CenAlign): PolyParent =
    new PolyParent(cen, poly, evObj, Refs(poly.fill(fillColour), TextGraphic(str, fontSize, poly.polyCentre, fontColour, align)))

  def fillContrastText(cen: Vec2, poly: Polygon, evObj: Any, fillColour: Colour, str: String, fontSize: Int = 4): PolyParent =
    fillText(cen, poly, evObj, fillColour, str, fontSize, fillColour.contrast)
}
