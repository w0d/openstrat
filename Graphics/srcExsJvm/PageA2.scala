/* Copyright 2018-20 Licensed under Apache Licence version 2.0. */
package ostrat
package geom
package pExs
import pWeb._

object PageA2  extends LessonPage
{ val head = HtmlHead(Arr(HtmlTitle("Lesson A2")))
  
  val r1 = Rect.applyOld(4, 5, Vec2Z)
  
  val bodyStr =
    """<h1>Lesson A2</h1>
      |<p>This lesson covers Circles and Ellipses. As with the other lessons there will be a summary for those familiar with Scala and anumber of step
      |by step parts for those new to Scala, programming or geometry and graphics. </p>""".stripMargin
  override val page: HtmlPage = HtmlPage(head, HtmlBody(bodyStr))
}