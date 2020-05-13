/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
package geom
import reflect.ClassTag

trait MirrorAxis[T]
{ /** Reflect, mirror across the X axis. */
  def mirrorXOffset(yOffset: Double): T


  /** Reflect, mirror across the Y axis. */
  def mirrorYOffset(xOffset: Double): T
}

object MirrorAxis
{

}