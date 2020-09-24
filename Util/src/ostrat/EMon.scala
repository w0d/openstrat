/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
import scala.annotation.unchecked.uncheckedVariance

/** An Errors handling class. Consider changing name to EHan. The main ways to consume the final result of the flatMap operation are fold, getElse,
 * foreach and forEither. This corresponds, but is not functionally equivalent to an Either[StrList, A] or Either[List[String], +A]. There are
 * advantages to having a separate class and I find that I rarely use Either apart from with standard errors as the Left type. However use the
 * methods biMap, to Either, eitherMap and eitherFlatMap when interoperability with Either is required. In my view Either[T] class is redundant and is
 * rarely used except as an errors handler. So it makes sense to use a dedicated class. */
sealed trait EMon[+A]
{ def map[B](f: A => B): EMon[B]
  def flatMap[B](f: A => EMon[B]): EMon[B]
  def flatMap2[B1, B2](f: A => EMon2[B1, B2]): EMon2[B1, B2]
  /** Gets the value of Good or returns the elseValue parameter if Bad. Both Good and Bad should be implemented in the leaf classes to avoid
   * unnecessary boxing of primitive values. */
  def getElse(elseValue: A @uncheckedVariance): A
  /** This is called map for typeclass map. Hope to have this as the standard map. */
//  def baseMap[B, BB <: EMonBase[B]](f: A => B)(implicit build: EMonBuild[B, BB]): BB

  /** This is called map for typeclass map. Hope to have this as the standard map. */
  //def baseFlatMap[B, BB <: EMonBase[B]](f: A => BB)(implicit build: EMonBuild[B, BB]): BB

  def errs: Strings

  /** Will perform action if Good. Does nothing if Bad. */
  def forGood(f: A => Unit): Unit

  /** Fold the EMon of type A into a type of B. */
  @inline def foldErrs[B](fGood: A => B)(fBad: Strings => B): B

  /** This is just a Unit returning fold, but is preferred because the method  is explicit that it is called for effects, rather than to return a
   *  value. This method is implemented in the leaf Good classes to avoid boxing. */
  def foldDo(fGood: A => Unit)(fBad: Strings => Unit): Unit

  /** Gets the value of Good, throws exception on Bad. */
  def get: A

  def fold[B](noneValue: => B)(fGood: A => B): B
  def fld[B](noneValue: => B, fGood: A => B): B


  def toEither: Either[Strings, A]
  def isGood: Boolean
  def isBad: Boolean

  /** Maps Good to Right[Strings, D] and Bad to Left[Strings, D]. These are implemented in the base traits GoodBase[+A] and BadBase[+A] as
   *  Either[+A, +B] boxes all value classes. */
  def mapToEither[D](f: A => D): Either[Strings, D]

  /** These are implemented in the base traits GoodBase[+A] and BadBase[+A] as Either[+A, +B] boxes all value classes. */
  def flatMapToEither[D](f: A => Either[Strings, D]): Either[Strings, D]

  /** These are implemented in the base traits GoodBase[+A] and BadBase[+A] as Either[+A, +B] boxes all value classes. */
  def biMap[L2, R2](fLeft: Strings => L2, fRight: A => R2): Either[L2, R2]
}

object EMon
{
  implicit class EMonStringImplicit(thisEMon: EMon[String])
  { def findType[A](implicit ev: Persist[A]): EMon[A] = thisEMon.flatMap(str => pParse.stringToStatements(str).flatMap(_.findType[A]))
    def findTypeElse[A: Persist](elseValue: => A): A = findType[A].getElse(elseValue)
    def findTypeForeach[A: Persist](f: A => Unit): Unit = findType[A].forGood(f)
    def findSetting[A](settingStr: String)(implicit ev: Persist[A]): EMon[A] =
      thisEMon.flatMap(str => pParse.stringToStatements(str).flatMap(_.findSett[A](settingStr)))
    def findSettingElse[A: Persist](settingStr: String, elseValue: => A): A = findSetting[A](settingStr).getElse(elseValue)
    def findSomeSetting[A: Persist](settingStr: String, elseValue: => A): A = ??? //findSetting[Option[A]](settingStr)(implicit ev: Persist[A]): EMon[A]
    def findSomeSettingElse[A: Persist](settingStr: String, elseValue: => A): A = ??? //findSetting[A](settingStr).getElse(elseValue)
  }

  implicit def showImplicit[A](implicit ev: Show[A]): Show[EMon[A]] =
    ShowSum2("EMon", Good.GoodShowImplicit(ev),
      Bad.BadShowImplicit(ev))
}

/** The Good sub class of EMon[+A]. This corresponds, but is not functionally equivalent to an Either[List[String], +A] based
 *  Right[Refs[String], +A]. */
final case class Good[+A](val value: A) extends EMon[A] //with GoodBase[A]
{
  override def map[B](f: A => B): EMon[B] = Good[B](f(value))
  //override def baseMap[B, BB <: EMonBase[B]](f: A => B)(implicit build: EMonBuild[B, BB]): BB = build(f(value))
  //override def baseFlatMap[B, BB <: EMonBase[B]](f: A => BB)(implicit build: EMonBuild[B, BB]): BB = f(value)
  override def flatMap[B](f: A => EMon[B]): EMon[B] = f(value)
  @inline override def foldErrs[B](fGood: A => B)(fBad: Strings => B): B = fGood(value)

  override def fold[B](noneValue: => B)(fGood: A => B): B = fGood(value)
  override def fld[B](noneValue: => B, fGood: A => B): B = fGood(value)

 // @inline override def fld[B](fGood: A => B, fBad: Strings => B) : B = fGood(value)
  override def foldDo(fGood: A => Unit)(fBad: Strings => Unit): Unit = fGood(value)
  override def flatMap2[B1, B2](f: A => EMon2[B1, B2]): EMon2[B1, B2] = f(value)
  override def forGood(f: A => Unit): Unit = f(value)
  override def get: A = value
  override def getElse(elseValue: A @uncheckedVariance): A = value
  //def value: A
  override def errs: Strings = Arr()
  override def toEither: Either[Strings, A] = Right(value)
  override def isGood: Boolean = true
  override def isBad: Boolean = false
  override def mapToEither[D](f: A => D): Either[Strings, D] = Right(f(value))
  override def flatMapToEither[D](f: A => Either[Strings, D]): Either[Strings, D] = f(value)
  override def biMap[L2, R2](fLeft: Strings => L2, fRight: A => R2): Either[L2, R2] = Right(fRight(value))
}

object Good
{
  implicit def GoodShowImplicit[A](implicit ev: Show[A]): Show[Good[A]] = new Show[Good[A]] with ShowCompound[Good[A]]
  { override def syntaxDepth: Int = ev.syntaxDepth + 1
    override def typeStr: String = "Good" + ev.typeStr.enSquare
    override def showSemi(obj: Good[A]): String = ev.showSemi(obj.value)
    override def showComma(obj: Good[A]): String = ev.showComma(obj.value)
  }
}

/** The errors case of EMon[+A]. This corresponds, but is not functionally equivalent to an Either[List[String], +A] based Left[List[String], +A]. */
case class Bad[+A](errs: Strings) extends EMon[A] //with BadBase[A]
{ override def map[B](f: A => B): EMon[B] = Bad[B](errs)
 // override def baseMap[B, BB <: EMonBase[B]](f: A => B)(implicit build: EMonBuild[B, BB]): BB = build.newBad(errs)
  //override def baseFlatMap[B, BB <: EMonBase[B]](f: A => BB)(implicit build: EMonBuild[B, BB]): BB = build.newBad(errs)
  override def flatMap[B](f: A => EMon[B]): EMon[B] = Bad[B](errs)
  override def fold[B](noneValue: => B)(fGood: A => B): B = noneValue
  override def fld[B](noneValue: => B, fGood: A => B): B = noneValue
  @inline override def foldErrs[B](fGood: A => B)(fBad: Strings => B): B = fBad(errs)

  //@inline override def fld[B](fGood: A => B, fBad: Strings => B) : B = fBad(errs)

  override def flatMap2[B1, B2](f: A => EMon2[B1, B2]): EMon2[B1, B2] = new Bad2[B1, B2](errs)

  override def getElse(elseValue: A @uncheckedVariance): A = elseValue
 // override def elseTry[A1 >: A](otherValue: EMon[A1]): EMon[A1] = otherValue
 override def forGood(f: A => Unit): Unit = {}
  override def toEither: Either[Strings, A] = Left(errs)
  override def get: A = excep("Called get on Bad.")
  override def foldDo(fGood: A => Unit)(fBad: Strings => Unit): Unit = fBad(errs)
  // override def mapArr[B, BB <: ArrImut[B]](f: A => B)(implicit build: ArrBuild[B, BB]): BB = build.imutNew(0)

  override def isGood: Boolean = false
  override def isBad: Boolean = true
  override def mapToEither[D](f: A => D): Either[Strings, D] = Left(errs)
  override def flatMapToEither[D](f: A => Either[Strings, D]): Either[Strings, D] = (Left(errs))
  override def biMap[L2, R2](fLeft: Strings => L2, fRight: A => R2): Either[L2, R2] = Left(fLeft(errs))

}

object Bad
{
  implicit def BadShowImplicit[A](implicit ev: Show[A]): Show[Bad[A]] = new Show[Bad[A]] with ShowCompound[Bad[A]]
  {
    override def syntaxDepth: Int = 2
    override def typeStr: String = "Bad" + ev.typeStr.enSquare
    override def showSemi(obj: Bad[A]): String = obj.errs.mkString("; ")
    override def showComma(obj: Bad[A]): String = ??? // obj.errs.semiFold
  }
}