/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
package pParse

/** The top level compositional unit of Syntax in CRON: Compact Readable Object Notation. A statement can be claused consisting of comma separated
  * clauses containing a single expression. An empty statement is a special case of the UnClausedStatement where the semicolon character is the
  * expression. */
sealed trait Statement extends TextSpan
{ def optSemi: Opt[SemicolonToken]
  def errGet[A](implicit ev: Persist[A]): EMon[A]
  def expr: Expr
}

object Statement
{
  implicit class StatementListImplicit(statementList: List[Statement]) extends TextSpan
  { private def ifEmptyFilePosn: FilePosn = FilePosn("Empty Statement Seq", 0, 0)
    def startPosn = statementList.ifEmpty(ifEmptyFilePosn, statementList.head.startPosn)
    def endPosn = statementList.ifEmpty(ifEmptyFilePosn, statementList.last.endPosn)
    
    def errGet1[A1](implicit ev1: Persist[A1]): EMon[(A1)] = statementList match
    { case Seq(h1) => h1.errGet[A1](ev1)
      case s => bad1(s, s.length.toString -- "statements not 1")
    }
  
    def errGet2[A1, A2](implicit ev1: Persist[A1], ev2: Persist[A2]): EMon[(A1, A2)] = statementList match
    { case Seq(h1, h2) => h1.errGet[A1](ev1).map2(h2.errGet[A2](ev2), (g1, g2: A2) => (g1, g2))
      case s => bad1(s, s.length.toString -- "statements not 2")
    }
    
    def errGet3[A1, A2, A3](implicit ev1: Persist[A1], ev2: Persist[A2], ev3: Persist[A3]): EMon[(A1, A2, A3)] =
      statementList match
    { case Seq(h1, h2, h3) => h1.errGet[A1](ev1)map3(h2.errGet[A2](ev2), h3.errGet[A3](ev3), (g1: A1, g2: A2, g3: A3) => (g1, g2, g3))
      case s => bad1(s, s.length.toString -- "statements not 3")
    }
    def errGet4[A1, A2, A3, A4](implicit ev1: Persist[A1], ev2: Persist[A2], ev3: Persist[A3], ev4: Persist[A4]):
       EMon[(A1, A2, A3, A4)] = statementList match
    {
       case Seq(h1, h2, h3, h4) => h1.errGet[A1](ev1)map4(h2.errGet[A2](ev2), h3.errGet[A3](ev3), h4.errGet[A4],
             (g1: A1, g2: A2, g3: A3, g4: A4) => (g1, g2, g3, g4))
       case s => bad1(s, s.length.toString -- "statements not 4")
    }

    def errFun2[A1, A2, B](f2: (A1, A2) => B)(implicit ev1: Persist[A1], ev2: Persist[A2]): EMon[B] = errGet2[A1, A2].map(f2.tupled(_))

    def errFun3[A1, A2, A3, B](f3: (A1, A2, A3) => B)(implicit ev1: Persist[A1], ev2: Persist[A2], ev3: Persist[A3]): EMon[B] =
       errGet3[A1, A2, A3].map(f3.tupled(_))

    def errFun4[A1, A2, A3, A4, B](f4: (A1, A2, A3, A4) => B)(implicit ev1: Persist[A1], ev2: Persist[A2], ev3: Persist[A3],
          ev4: Persist[A4]): EMon[B] = errGet4[A1, A2, A3, A4].map(f4.tupled(_))

    def findType[A](implicit ev: Persist[A]): EMon[A] = ev.findFromStatementList(statementList)
    /** Find unique instance of type from RSON statement. The unique instance can be a plain value or setting. If no value or duplicate values found
     *  use elseValue. */
    def findTypeElse[A](elseValue: A)(implicit ev: Persist[A]): A = findType[A].getElse(elseValue)
    def findTypeIndex[A](index: Int)(implicit ev: Persist[A]): EMon[A] =
    {
      val list = ev.listFromStatementList(statementList)
      if (list.length > index) Good(list(index))
        else bad1(FilePosn.empty, "Element " + index.toString -- "of" -- ev.typeStr -- "not found")       
    }
    def findInt: EMon[Int] = Persist.IntPersist.findFromStatementList(statementList)
    def findDouble: EMon[Double] = Persist.DoublePersist.findFromStatementList(statementList)
    def findBoolean: EMon[Boolean] = Persist.BooleanPersist.findFromStatementList(statementList)
    def findIntArray: EMon[Array[Int]] = Persist.IntArrayToPersist.findFromStatementList(statementList)   
    
    
    /** Find setting from RSON statement */
    def findSett[A](settingSym: Symbol)(implicit ev: Persist[A]): EMon[A] = ev.settingFromStatementList(statementList, settingSym)
    def findSettElse[A](settingSym: Symbol, elseValue: A)(implicit ev: Persist[A]): A = findSett[A](settingSym).getElse(elseValue)
    def findIntSett(settingSym: Symbol): EMon[Int] = Persist.IntPersist.settingFromStatementList(statementList, settingSym)
    def findDoubleSett(settingSym: Symbol): EMon[Double] = Persist.DoublePersist.settingFromStatementList(statementList, settingSym)
    def findBooleanSett(settingSym: Symbol): EMon[Boolean] = Persist.BooleanPersist.settingFromStatementList(statementList, settingSym)
  }

  implicit class EmonStatementListImplict(eMon: EMon[List[Statement]])
  {
    def errGet1[A1](implicit ev1: Persist[A1]): EMon[(A1)] = eMon.flatMap(_.errGet1[A1])
  }
}

/** This statement has 1 or more comma separated clauses. If there is only 1 Clause, it must be terminated by a comma, otherwise the trailing comma
 *  on the last Clauses is optional. */
case class ClausedStatement(clauses: List[Clause], optSemi: Opt[SemicolonToken]) extends Statement with TextSpanMems
{
  def expr: Expr = ClausesExpr(clauses.map(_.expr))
  def startMem: TextSpan = clauses.head
  def endMem: TextSpan = optSemi.fold[TextSpan](clauses.last, st => st)
  override def errGet[A](implicit ev: Persist[A]): EMon[A] = ev.fromClauses(clauses)
}

/** An unclaused Statement has a single expression. */
sealed trait UnClausedStatement extends Statement
{ def expr: Expr
  def optSemi: Opt[SemicolonToken]
  override def errGet[A](implicit ev: Persist[A]): EMon[A] = ev.fromExpr(expr)
}

/** An un-claused Statement that is not the empty statement. */
case class MonoStatement(expr: Expr, optSemi: Opt[SemicolonToken]) extends UnClausedStatement with TextSpanMems
{
  def startMem: TextSpan = expr
  def endMem: TextSpan = optSemi.fold(expr, sc => sc)
}

/** The Semicolon of the Empty statement is the expression of this special case of the unclaused statement */
case class EmptyStatement(st: SemicolonToken) extends UnClausedStatement with TextSpanMems
{
   override def expr = st
   override def optSemi: Opt[SemicolonToken] = Opt(st)
   override def startMem: TextSpan = st
   override def endMem: TextSpan = st
   def asError[A]: Bad[A] = bad1(st.startPosn, "Empty Statement")
}
object EmptyStatement
{
   def apply(st: SemicolonToken): EmptyStatement = new EmptyStatement(st)   
}
