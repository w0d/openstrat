/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package learn
import ostrat._, geom._, pCanv._, Colour._

case class LessonC5(canv: CanvasPlatform) extends CanvasSimple("Lesson C5")
{
  val r1 = ARect(Vec2Z, 500, 300)
  val r2 = ARect(Vec2Z, 400, 250)
  val r3 = ARect(-200 vv 0)
  val r4 = ARect(250 vv 150)
  val r5 = ARect(Vec2Z, 100, 500)
  var rList = List(r1, r2, r3, r4, r5)
  def gList = rList.map(_.sGraphic)  
  val startText = TextGraphic("Click on the rectangles. All rectangles under the point will cycle their colour.", 28, 0 vv 400)
  repaint(gList :+ startText)  
  
  mouseUp = (v, b, s) => 
  {
    s.foreach{ obj =>
      val r = obj.asInstanceOf[ARect] 
      rList = rList.replace(r, r.mutateColour(r.colour.nextFromList(rainbow)))
    }    
    repaint(gList :+ startText)          
  }
}