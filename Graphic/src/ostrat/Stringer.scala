/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat

/** A variation on the Show trait for traits, classes and objects that you control. Named after the popular "The Wire" character. You will still need to
  * create an implicit Persist object in the companion object of your type, but this trait does allow you to automatically delegate toString to the
  * String / Persist implementation. */
trait Stringer extends Any
{ def typeSym: Symbol
  def typeStr: String = typeSym.name
  def str: String
  final override def toString = str
  
  def persist1[T1](v1: T1)(implicit ev1: Persist[T1]): String = typeStr + ev1.persist(v1).enParenth
  def persistD1(d1: Double): String = typeStr + (d1.toString).enParenth
  
  def persist2[T1, T2](v1: T1, v2: T2)(implicit ev1: Persist[T1], ev2: Persist[T2]): String =
    typeStr + ev1.persistComma(v1).semicolonAppend(ev2.persistComma(v2)).enParenth
  
  def persistD2(d1: Double, d2: Double): String = typeStr + (d1.toString + "; " + d2.toString).enParenth
  
  def persist3[T1, T2, T3](v1: T1, v2: T2, v3: T3)(implicit ev1: Persist[T1], ev2: Persist[T2], ev3: Persist[T3]): String =
    typeStr + ev1.persistComma(v1).semicolonAppend(ev2.persistComma(v2), ev3.persistComma(v3)).enParenth
  
  def persistD3(d1: Double, d2: Double, d3: Double): String = typeStr + List(d1, d2, d3).map(_.toString).semicolonFold.enParenth
  
  def persist4[T1, T2, T3, T4](v1: T1, v2: T2, v3: T3, v4: T4)(implicit ev1: Persist[T1], ev2: Persist[T2], ev3: Persist[T3], ev4: Persist[T4]):
  String = typeStr + ev1.persistComma(v1).semicolonAppend(ev2.persistComma(v2), ev3.persistComma(v3), ev4.persistComma(v4)).enParenth
  
  def persist5[T1, T2, T3, T4, T5](v1: T1, v2: T2, v3: T3, v4: T4, v5: T5)(implicit ev1: Persist[T1], ev2: Persist[T2], ev3: Persist[T3],
      ev4: Persist[T4], ev5: Persist[T5]): String =
  { val mems: List[String] = List(ev1.persistComma(v1), ev2.persistComma(v2), ev3.persistComma(v3), ev4.persistComma(v4), ev5.persistComma(v5)) 
    typeStr + mems.semicolonFold.enParenth
  }
  def persist6[T1, T2, T3, T4, T5, T6](v1: T1, v2: T2, v3: T3, v4: T4, v5: T5, v6: T6)(implicit ev1: Persist[T1], ev2: Persist[T2], ev3: Persist[T3],
      ev4: Persist[T4], ev5: Persist[T5], ev6: Persist[T6]): String =
  {
    val mems: List[String] = List(ev1.persistComma(v1), ev2.persistComma(v2), ev3.persistComma(v3), ev4.persistComma(v4), ev5.persistComma(v5),
        ev6.persistComma(v6))     
    typeStr + mems.semicolonFold.enParenth   
  }
}