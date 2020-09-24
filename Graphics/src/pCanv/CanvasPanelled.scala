/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package pCanv
import geom._

/** A canvas divided up into panels. Each panel is clipped and has its own origin. */
abstract class CanvasPanelled(title: String) extends CanvasUser(title)
{
  var panels: List[Panel] = Nil
  
  def addPanel(clipPoly: PolygonGen, cover: Boolean = true): Panel =
  { val newPanel = Panel(this, clipPoly, cover)
    panels :+= newPanel
    newPanel
  }     
  
  canv.mouseUp = (v, b) =>
    {
      panels.find(_.clipPoly.ptInside(v)).foreach{ pan =>
        val ids: List[Any] = pan.actives.filterToList(_.ptInside(v)).map(_.pointerId)
        pan.mouseUp(b, ids, v)
    }
  }
    
  def refresh(): Unit = panels.foreach(refreshPanel)   
   
  def refreshPanel(panel: Panel): Unit =
  { val clipPoly = panel.clipPoly
    canv.gcSave()
    canv.clip(clipPoly)
    canv.polyFill(clipPoly, panel.backColour)
    val movedObjs: Arr[GraphicElem] = panel.canvObjs.slate(panel.clipCen)
    panel.actives = paintObjs(movedObjs)
    canv.gcRestore()
  }   
}