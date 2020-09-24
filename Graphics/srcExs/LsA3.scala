/* Copyright 2018-20 Licensed under Apache Licence version 2.0. */
package learn
import ostrat._, geom._, pCanv._, Colour._

case class LsA3(canv: CanvasPlatform) extends CanvasNoPanels("Lesson A3")
{
  val x1 = 100
  val y1 = 200
  
  val c1 = Circle(100).fill(Green)
  val c1d = c1.scale2.toDraw()
  val circles = Arr(c1, Circle(100, 0, 200).fill(Violet), Circle(100, 200, 0).fill(SandyBrown), Circle(100, 0, -200).fill(Turquoise), c1d)
  val crosses =  Vec2s(0 vv 0, -100 vv 0, 100 vv 0, 0 vv 100).flatMap(v => Cross(1, v))

  val el1 = Ellipse(200, 100, -300 vv 0).fill(Red)
  val el2 = Ellipse(50, 100, 150 vv 200).fill(Colour.BurlyWood)
  val el3 = el2.xSlate(100).toDraw(2)
  val els = Arr(el1, el2, el3)
  repaint(circles ++ crosses ++ els)
}