/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
import pParse._

/** The base trait for the persistence of Case classes, aka Product types */
abstract class PersistCase[R](typeSym: Symbol) extends PersistCompound[R](typeSym)
{ def persistMems: List[Persist[_]]
  final override def syntaxDepth: Int = persistMems.map(_.syntaxDepth).max + 1  
}

/** Persistence class for single parameter case classes. 2 Methods not implemented. not sure about this class or its sub class PersistD1. */
class Persist1[A1, R](typeSym: Symbol, val fParam: R => A1, val newT: A1 => R)(implicit ev1: Persist[A1]) extends
   PersistCase[R](typeSym)
{ def persistMems = List(ev1)  
  def persistSemi(obj: R): String = ev1.persistComma(fParam(obj))
  def persistComma(obj: R): String = ev1.persist(fParam(obj))  
  def fromClauses(clauses: Seq[Clause]): EMon[R] = ???// fromClauses1(newT, clauses)
  def fromParameterStatements(sts: Seq[Statement]): EMon[R] = ???// sts.errFun1(newT)(ev1)   
}

/** Persistence class for case classes taking a single Double parameter. Not sure about this class. It is currently being used for Double based value
 *  classes. I think this is wrong and that they need their own trait class. */
class PersistD1[R](typeSym: Symbol, fParam: R => Double, newT: Double => R) extends Persist1[Double, R](typeSym, fParam, newT)

/** Persistence class for 2 parameter case classes. */ 
class Persist2[A1, A2, R](typeSym: Symbol, val fParam: R => (A1, A2), val newT: (A1, A2) => R)(implicit ev1: Persist[A1], ev2: Persist[A2])
   extends PersistCase[R](typeSym)
{ def persistMems = List(ev1, ev2)
   
  override def persistSemi(obj: R): String =
  { val (a1, a2) = fParam(obj)
    ev1.persistComma(a1) + "; " + ev2.persistComma(a2)
  }
  
  override def persistComma(obj: R): String =
  { val (a1, a2) = fParam(obj)
    ev1.persist(a1) + ", " + ev2.persist(a2)
  }
   
  override def fromClauses(clauses: Seq[Clause]): EMon[R] = fromClauses2(newT, clauses)
  override def fromParameterStatements(sts: Seq[Statement]): EMon[R] = sts.errFun2(newT)(ev1, ev2)   
}

/** Persistence class for case classes consisting of 2 Double parameters. */
abstract class PersistD2[R](typeSym: Symbol, fParam: R => (Double, Double), newT: (Double, Double) => R) extends
   Persist2[Double, Double, R](typeSym, fParam, newT)

/** Persistence class for 3 parameter case classes. */   
abstract class Persist3[A1, A2, A3, R](typeSym: Symbol, val fParam: R => (A1, A2, A3), val newT: (A1, A2, A3) => R)(
    implicit ev1: Persist[A1], ev2: Persist[A2], ev3: Persist[A3]) extends PersistCase[R](typeSym)
{ def persistMems = List(ev1, ev2, ev3)
  
  override def persistSemi(obj: R): String =
  { val (p1, p2, p3) = fParam(obj)
    ev1.persist(p1).semicolonAppend(ev2.persistComma(p2), ev3.persistComma(p3))
  }

  override def persistComma(obj: R): String =
  { val (p1, p2, p3) = fParam(obj)
    ev1.persist(p1).commaAppend(ev2.persist(p2), ev3.persist(p3))
  }

  override def fromClauses(clauses: Seq[Clause]): EMon[R] = fromClauses3(newT, clauses)
  override def fromParameterStatements(sts: Seq[Statement]): EMon[R] = sts.errFun3(newT)(ev1, ev2, ev3)
}

/** Persistence class for case classes consisting of 3 Double parameters. */
abstract class PersistD3[R](typeSym: Symbol, fParam: R => (Double, Double, Double), newT: (Double, Double, Double) => R) extends
   Persist3[Double, Double, Double, R](typeSym, fParam, newT)

/** Persistence class for 4 parameter case classes. */   
abstract class Persist4[A1, A2, A3, A4, R](typeSym: Symbol, val newT: (A1, A2, A3, A4) => R, val fParam: R => (A1, A2, A3, A4))(
    implicit ev1: Persist[A1], ev2: Persist[A2], ev3: Persist[A3], ev4: Persist[A4]) extends PersistCase[R](typeSym)
{ def persistMems = List(ev1, ev2, ev3, ev4)
  override def persistSemi(obj: R): String =
  { val (p1, p2, p3, p4) = fParam(obj)
    ev1.persist(p1).semicolonAppend(ev2.persist(p2), ev3.persist(p3), ev4.persist(p4))
  }
  override def fromClauses(clauses: Seq[Clause]): EMon[R] = fromClauses4(newT, clauses)
  override def fromParameterStatements(sts: Seq[Statement]): EMon[R] = sts.errFun4(newT)(ev1, ev2, ev3, ev4)
}