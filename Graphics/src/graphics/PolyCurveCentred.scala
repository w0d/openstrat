/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package geom

/** So there is a lack of clarity over whether the segs are relative to the cen, and if the cen is needed at all. */
case class PolyCurveCentred(cen: Vec2, segs: PolyCurve) extends AffinePreserve
{ override type ThisT = PolyCurveCentred
   /** This may need clarification */
   override def fTrans(f: Vec2 => Vec2): PolyCurveCentred = PolyCurveCentred(f(cen), segs)//.fTrans(f))
  
  def parentAll(evObj: Any, fillColour: Colour, lineWidth: Double, lineColour: Colour, textSize: Int, str: String,
                   textAlign: TextAlign = CenAlign): PolyCurveParentFull =
    PolyCurveParentFull(cen, segs, evObj, Arr(PolyCurveFillDraw(segs, fillColour, lineWidth, lineColour), TextGraphic(str, textSize, cen, lineColour, textAlign)))

  def allElems(evObj: Any, fillColour: Colour, lineWidth: Double, lineColour: Colour, fontSize: Int, str: String, textAlign: TextAlign = CenAlign):
    PolyCurveAll = PolyCurveAll(segs, evObj, str, fillColour, fontSize, lineWidth, lineColour)
}