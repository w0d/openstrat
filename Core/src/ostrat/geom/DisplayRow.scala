/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package geom

/** This is not well documented */
case class DisplayRow(margin: Double, subjs: List[GraphicSubject[_]])
{
   def fromLeft(leftPt: Vec2): List[GraphicSubject[_]] =
   {      
      def loop(rem: Seq[GraphicSubject[_]], acc: List[GraphicSubject[_]], x: Double): List[GraphicSubject[_]] = rem.fMatch(
            acc,
            (head, tail) =>
               loop(tail,
                     acc :+
                     head.slateX(
                           {
                              if (head == null) println("DiplayRow null " + rem.length)
                           x + head.width / 2}).
                        asInstanceOf[GraphicSubject[_]],
                     x + head.width + margin)
            )
      loop(subjs, Nil, leftPt.x + margin)
   }
  
}