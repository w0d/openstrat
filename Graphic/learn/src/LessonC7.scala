package learn
import ostrat._, geom._, pCanv._, Colour._

/** This lesson displays an interactive Bezier curve whose points can be dragged and also displays the syntax required to draw it */
case class LessonC7(canv: CanvasPlatform) extends CanvasNoPanels("Lesson C7: Exploring Beziers")
{ /** defines the size of the circles that represent the points of the bezier */
  val circleRadius = 20
  case class Drag(var v: Vec2, c: Colour)
  
  /** start point bezier. */
  val p1 = Drag(-100 vv 0, Gray)
  /** End point of bezier curve. */
  val p2 = Drag(100 vv  0, Gray)
  /** control point for start point */
  val c1 = Drag(-100 vv -250, Red)
  /** control point for end point */
  val c2 = Drag(100 vv 50, Red)    
  val pts = Arr(p1, p2, c1, c2)

  /** when one of the bezier points is being dragged, this will indicate which */
  var theDragee: Option[Drag] = None 
  
  drawBezier()

  def drawBezier():Unit = 
  { val cds = pts.map(dr => Circle(circleRadius, dr.v).fill(dr.c))
    
    val cl1 = LineDraw(p1.v, c1.v, 1, Grey)    /** line between the start point and its control point */
    val cl2 = LineDraw(p2.v, c2.v, 1, Grey)    /** line between the end point and its control point */

    val bez = BezierDraw(p1.v, c1.v, c2.v, p2.v, 2, Green) /** the bezier to be displayed */

    /** this holds the syntax required to draw the current bezier (bez) (NB: replace ; with , ) */
    val txt = TextGraphic("BezierDraw(" + p1.v + ", " + c1.v + ", " + c2.v + ", " + p2.v + ", 2, Green)", 18, 0 vv 300)

    val elementsToPaint = cds ++ Arr(txt, cl1, cl2, bez)

    repaint(elementsToPaint)
  }

  /* test to see if drag operation has started. if the mouseDown is on one of the represented bezier points then set theDragee to its corresponding
   option */
  canv.mouseDown = (position, _) => theDragee = pts.find(i => (i.v - position).magnitude <= circleRadius)

  // When a point is being dragged update the correspondin bezier point with its new position and then redraw the screen. */
  canv.mouseDragged = (position, button) => theDragee match
  { case Some(drag) => drag.v = position; drawBezier()
    case _ => theDragee = None
  }

  /** dragging has finished so reset theDragee */
  mouseUp = (button, clickList, position) => theDragee = None 
}