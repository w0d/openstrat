/* Copyright 2018-20 Licensed under Apache Licence version 2.0. */
package ostrat
package geom
package pExs
import pWeb._

object PageA1 extends LessonPage
{ val head = HtmlHead(Arr(HtmlTitle("Lesson A1")))
  val bodyStr =
    """<h1>Lesson A1</h1>
      |<p>This lesson covers Circles and Ellipses. As with the other lessons there will be a summary for those familiar with Scala and anumber of step
      |by step parts for those new to Scala, programming or geometry and graphics. </p>""".stripMargin
  override val page: HtmlPage = HtmlPage(head, HtmlBody(bodyStr))    
}
