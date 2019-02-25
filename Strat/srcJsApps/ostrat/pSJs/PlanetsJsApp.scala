/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pSJs
import scalajs.js.annotation._

@JSExportTopLevel("PlanetsJsApp")
object PlanetsJsApp
{
   @JSExport def main(args: Array[String]): Unit = pGames.pSpace.Planets(CanvasJs)  
}


