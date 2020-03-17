/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pCanv
import geom._

/** So the descendant classes need to set the canv.mouseup field to use the mouse and its equivalents. */
abstract class CanvasUser(val title: String)
{
  val canv: CanvasPlatform
  /** This reverses the order of the GraphicActive List. Method paints objects to screen as side effect. */
  def paintObjs(movedObjs: GraphicElems): Refs[GraphicActive] =
  {
    val activejBuff: Buff[GraphicActive] = Buff()
    movedObjs.foreach {
      case el: GraphicActive => activejBuff += el
      case _ =>
    }

    movedObjs.foreach
    { case ce: PaintElem => ce.rendElem(canv)

      case cs: GraphicParent =>
      { canv.rendElems(cs.elems)
        //activejBuff += cs
      }

      case cs: GraphicParentOld =>
      { canv.rendElemsOld(cs.elems)
        //activejBuff += cs
      }

      case nss: UnScaledShape =>
      { canv.rendElems(nss.elems.slate(nss.referenceVec))
        //activejBuff += nss
      }

       case ga: GraphicActive => //activejBuff += ga
    }
    activejBuff.toReverseRefs
  }
   
  def refresh(): Unit   
  canv.resize = () => refresh()   
}