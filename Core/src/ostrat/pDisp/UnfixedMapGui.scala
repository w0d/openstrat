/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pDisp

/** So currently this is a turn based class allowing the selection of objects within the map. That is not the case for the Planet App 
 *  where the planets move too quickly for selection. Why it is called "Unfixed" I have no idea. */
<<<<<<< HEAD
abstract class UnfixedMapGui extends pDisp.MapGui
=======
abstract class UnfixedMapGui extends MapGui
>>>>>>> 72180359d6a0d019c6b8e22992d05276ee86c59d
{
   var selected: List[AnyRef] = Nil
   def inCmd: MouseButton => Unit  
   def outCmd: MouseButton => Unit
   def leftCmd: MouseButton => Unit  
   def rightCmd: MouseButton => Unit  
   def downCmd: MouseButton => Unit    
   def upCmd: MouseButton => Unit 
   
   val bIn = button3("+", inCmd)   
   val bOut = button3("-", outCmd)   
   val bLeft = button3("<", leftCmd)   
   val bRight = button3(">", rightCmd)   
   val bDown = button3("v", downCmd)   
   val bUp = button3("^", upCmd)
   
   val guButs = Seq(bIn, bOut, bLeft, bRight, bDown, bUp)
}