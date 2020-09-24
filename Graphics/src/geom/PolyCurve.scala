/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package geom
import Colour.Black

/** Shape is an Array[Double] based collection for a sequence of CurveSegs, similar to a Polygon which is an Array[Double based collection of just
 *   LineSegs. It Uses 6 Doubles for each CurveSeg. The first Double of each curveSeg is set to Negative Infinity for a LineSeg positive infinity for
 *   an ArcSeg, but represents the x component of the first control point for a BezierSeg. */
class PolyCurve(val arrayUnsafe: Array[Double]) extends ArrProdDbl7[CurveTail] with AffinePreserve
{ type ThisT = PolyCurve
  def unsafeFromArray(array: Array[Double]): PolyCurve = new PolyCurve(array)
  override def typeStr = "Shape"
  override def fElemStr: CurveTail => String = _.toString
  override def newElem(iMatch: Double, d1: Double, d2: Double, d3: Double, d4: Double, d5: Double, d6: Double): CurveTail =
    new CurveTail(iMatch, d1, d2, d3, d4, d5, d6)

  override def canEqual(that: Any): Boolean = ???

  override def productArity: Int = ???

  override def productElement(n: Int): Any = ??? 
  def fTrans(f: Vec2 => Vec2): PolyCurve =
  { val newArray = new Array[Double](length * 7)
    def setMiddle(offset: Int): Unit =
    { val newMiddle: Vec2 = f(arrayUnsafe(offset + 3) vv arrayUnsafe(offset + 4))
      newArray(offset + 3) = newMiddle.x
      newArray(offset + 4) = newMiddle.y
    }

    def setEnd(offset: Int): Unit =
    { val newEnd: Vec2 = f(arrayUnsafe(offset + 5) vv arrayUnsafe(offset + 6))
      newArray(offset + 5) = newEnd.x
      newArray(offset + 6) = newEnd.y
    }

    (0 until length).foreach{index =>
      val offset = index * 7
      arrayUnsafe(offset) match
      {
        case 10 =>
        { newArray(offset) = 10
          setEnd(offset)
        }

        case 11 =>
        { newArray(offset) = 11
          setMiddle(offset)
          setEnd(offset)
        }

        case 12 =>
        { newArray(offset) = 12
          val newControl1: Vec2 = f(arrayUnsafe(offset + 1) vv arrayUnsafe(offset + 2))
          newArray(offset + 1) = newControl1.x
          newArray(offset + 2) = newControl1.y
          setMiddle(offset)
          setEnd(offset)
        }

        case n => excep("iMatch in LineSeg has value: " + n.toString + " Must be 10, 11 0r 12.")
      }
    }
    new PolyCurve(newArray)
  }

  def fill(colour: Colour): PolyCurveFill = PolyCurveFill(this, colour)
  def draw(lineWidth: Double, lineColour: Colour = Black) = PolyCurveDraw(this,lineWidth, lineColour)
  def fillDraw(fillColour: Colour, lineWidth: Double, lineColour: Colour = Black) = PolyCurveFillDraw(this, fillColour, lineWidth, lineColour)
  def fillDrawClick(evObj: AnyRef, fillColour: Colour, lineWidth: Double, lineColour: Colour = Black): List[GraphicAffineElem] =
    List(PolyCurveFillDraw(this, fillColour, lineWidth, lineColour), PolyCurveActiveOnly(this, evObj))

  def shapeAll(shape: PolyCurve, evObj: AnyRef, fillColour: Colour, str: String, fontSize: Int = 24, lineWidth: Double = 2, lineColour: Colour = Black):
    PolyCurveAll = PolyCurveAll(shape, evObj, str, fillColour, fontSize, lineWidth, lineColour)

 // def fillSlateable(colour: Colour, evObj: AnyRef, posn: Vec2 = Vec2Z): UnScaledShape = UnScaledShape(posn, this, evObj, Arr(PolyCurveFill(this, colour)))
  def fillScale(colour: Colour, factor: Double): PolyCurveFill = PolyCurveFill(this.scale(factor), colour)
  def fillScaleSlate(colour: Colour, factor: Double, offset: Vec2): PolyCurveFill = PolyCurveFill(this.scale(factor).slate(offset), colour)

  /** Not sure if this method should be a member of Transable */
  def boundingRect =
  { //val t = Arc()
    var minX, maxX, minY, maxY = 0.0
    var i = 0
    this.foreach {ss =>
      val v = ss.pEnd
      if (i == 0)
      { minX = v.x
        maxX = v.x
        minY = v.y
        maxY = v.y
      }
      else
      { minX = minX.min(v.x)
        maxX = maxX.max(v.x)
        minY = minY.min(v.y)
        maxY = maxY.max(v.y)
      }
      i += 1
    }
    if (i == 0) throw new Exception("boundingRect method called on empty Vec2 collection") else {}
    BoundingRect(minX, maxX, minY, maxY)
  }
  def ptInShape: Vec2 => Boolean = pt =>  pMap[Vec2, PolygonGen](_.pEnd).ptInside(pt)

  /** Not sure if this is useful */
  def tailForeach(fLineSeg: CurveTail => Unit, fArcSeg: CurveTail => Unit, fBezierSeg: CurveTail => Unit): Unit =
    foreach(_.segDo(fLineSeg, fArcSeg, fBezierSeg))
}

object PolyCurve extends ProdDbl7sCompanion[CurveTail, PolyCurve]
{ implicit val factory: Int => PolyCurve = i => new PolyCurve(new Array[Double](i * 7))
}