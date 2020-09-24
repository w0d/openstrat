/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0 */

/** This is the root package for the Openstrat project. The top of this package contains, 32 bit Int based Colours, the Multiple type class, a
 *  show and persistence library using RCON (Name may change), Rich Compact Object Notation, array based compound value collections of same length
 *   elements, an Either based errors framework and general utilities. */
package object ostrat
{ import collection.mutable.ArrayBuffer, reflect.ClassTag
  type Buff[A] = ArrayBuffer[A]
  type ERefs[A <: AnyRef] = EMon[Arr[A]]
  type RefsMulti[A <: AnyRef] = Arr[Multiple[A]]
  type PersistEq[A] = Persist[A] with Eq[A]
  type ShowEq[A] = Show[A] with Eq[A]
  type AnyRefs = Arr[AnyRef]
  type Strings = Arr[String]
  type Not[T] = { type L[U] = U NotSubTypeOf T }

  val Tan30 = 0.577350269f;
  val Cos30 = 0.866025404f;
  val Cos60 = 0.5
  val Sin30 = 0.5
  val Sin60 = 0.866025404f;
  val Pi2 = math.Pi * 2
  val PiH = math.Pi / 2

  def NoRef[A <: AnyRef]: OptRef[A] = new OptRef[A](null.asInstanceOf[A])

  /** onlyIf-do. Only if the condition is true, perform the effect. */
  @inline def oif[U](b: Boolean, vTrue: => Unit): Unit = if(b) vTrue

  /** if-else. If the condition is true, use 2nd parameter, else use 3rd parameter. */
  @inline def ife[A](b: Boolean, vTrue: => A, vFalse: => A): A = if (b) vTrue else vFalse

  /** ifNot-else. If the condition is false, use 2nd parameter, else use 3rd parameter. */
  @inline def ifne[A](b: Boolean, vNotTrue: => A, visTrue: => A): A = if (b) vNotTrue else vNotTrue

  /** if-elseif-else. If the first condition is true, use 2nd parameter, else if the second condition in parameter 3 is true use 4th parameter. */
  @inline def ife2[A](b1: Boolean, vTrue1: => A, b2: => Boolean, vTrue2: => A, vElse: => A): A = if (b1) vTrue1 else if (b2) vTrue2 else vElse

  @inline def ife3[A](b1: Boolean, vTrue1: => A, b2: => Boolean, vTrue2: => A, b3: => Boolean, vTrue3: => A, vElse: => A): A =
    if (b1) vTrue1 else if (b2) vTrue2 else if (b3) vTrue3 else vElse

  @inline def ife4[A](b1: Boolean, vTrue1: => A, b2: => Boolean, vTrue2: => A, b3: => Boolean, vTrue3: => A, b4: => Boolean, vTrue4: => A, vElse: => A): A =
    if (b1) vTrue1 else if (b2) vTrue2 else if (b3) vTrue3 else if(b4) vTrue4 else vElse

  @inline def ife5[A](b1: Boolean, vTrue1: => A, b2: => Boolean, vTrue2: => A, b3: => Boolean, vTrue3: => A, b4: => Boolean, vTrue4: => A,
    b5: => Boolean, vTrue5: => A, vElse: => A): A =
    if (b1) vTrue1 else if (b2) vTrue2 else if (b3) vTrue3 else if(b4) vTrue4 else if(b5) vTrue5 else vElse

  @inline def if2Excep[A](b1: Boolean, vTrue1: => A, b2: Boolean, vTrue2: => A, excepStr: => String): A =
    if (b1) vTrue1 else if (b2) vTrue2 else  throw new Exception(excepStr)

  @inline def if3Excep[A](b1: Boolean, vTrue1: => A, b2: Boolean, vTrue2: => A, b3: Boolean, vTrue3: => A, excepStr: => String): A =
    if (b1) vTrue1 else if (b2) vTrue2 else if (b3) vTrue3 else throw new Exception(excepStr)

  @inline def excep(str: => String): Nothing = throw new Exception(str)
  @inline def ifExcep(b: Boolean, str: => String): Unit = if(b) throw new Exception(str)
  @inline def ifNotExcep(b: Boolean, str: => String): Unit = if(!b) throw new Exception(str)

  def eqOf[A](leftValue: A, rightValues: A *): Boolean = rightValues.contains(leftValue)



  /** Not sure what this method does. */
  def readT[T](implicit ev: Persist[T]): T =
  { val artStr = ev.typeStr.prependIndefiniteArticle
    def loop(inp: EMon[T]): T = inp match
    { case Good(t) => t
      case a =>
      { println(a)
        loop(io.StdIn.readLine ("That was not a single "+ ev.typeStr + ". Please enter " + artStr).asType[T])
      }
    }
    loop(io.StdIn.readLine ("Please enter " + artStr).asType[T])
  }

  def readInt: Int = readT[Int]
  def readDouble: Double = readT[Double]

  @inline def Buff[A](initialLength: Int = 5): ArrayBuffer[A] = new ArrayBuffer[A](initialLength)
  @inline def buffInt(initialLength: Int = 5): ArrayBuffer[Int] = new ArrayBuffer[Int](initialLength)

  type ParseExpr = pParse.Expr
  type RefTag[A] = AnyRef with reflect.ClassTag[A]

  /** Not sure about this method. */
  def parseErr(fp: TextPosn, detail: String): String = fp.fileName -- fp.lineNum.toString + ", " + fp.linePosn.toString + ": " + detail

  def bad1[B](fs: TextSpan, detail: String): Bad[B] = Bad[B](Arr(parseErr(fs.startPosn, detail)))

  def eTry[A](res: => A): EMon[A] =
    try Good[A](res) catch { case scala.util.control.NonFatal(e) => TextPosn("Java Exception", 1, 1).bad(e.getMessage) }

  def commaedInts(iSeq: Int*) = iSeq.map(_.toString).commaFold

  val two32: Long = 4294967296L
  def twoIntsToDouble(i1: Int, i2: Int): Double = { val lg  = (i1.toLong << 32) | (i2 & 0xFFFFFFFFL); java.lang.Double.longBitsToDouble(lg) }

  /** Not sure if this correct. This might throw on iStep = 0. */
  def iDblToMap[A, AA <: ArrBase[A]](iFrom: Double, iTo: Double, iStep: Double = 1)(f: Double => A)(implicit ev: ArrBuild[A, AA]): AA =
  { val iLen = (iTo - iFrom + 1).min(0) / iStep
    val res: AA = ev.newArr(iLen.toInt)
    var count = 0
    @inline def i: Double = iFrom + count * iStep

    while(i <= iTo)
    { ev.arrSet(res, count, f(i))
      count += 1
    }
    res
  }

  /** foreachs over a range of integers from parameter 1 to parameter 2 in steps of parameter 3. Throws on non termination. */
  def iToForeach(iFrom: Int, iTo: Int, iStep: Int = 1)(f: Int => Unit): Unit =
  { if (iTo == iFrom & iStep == 0) throw excep("Loop step can not be 0.")
    if (iTo > iFrom & iStep <= 0) throw excep("Loop step must be greater then 0. if to-value greater then from-value.")
    if (iTo < iFrom & iStep >= 0) throw excep("Loop step must be less then 0. if to-value less then from-value.")
    var i: Int = iFrom
    while(ife(iStep > 0, i <= iTo, i >= iTo)) { f(i); i += iStep }
  }

  /** foreachs over a range of integers from parameter 1 until parameter 2 (while index is less than) in steps of parameter 3. Throws on non
   *  termination. */
  def iUntilForeach(iFrom: Int, iUntil: Int, iStep: Int = 1)(f: Int => Unit): Unit =
    iToForeach(iFrom, ife(iStep > 0, iUntil - 1, iUntil + 1), iStep)(f)

  /** maps over a range of Ints to an ArrBase[A]. From the start value to (while index is less than or equal to) the end value in integer steps.
   *  Default step value is 1. */
  def iToMap[A, AA <: ArrBase[A]](iFrom: Int, iTo: Int, iStep: Int = 1)(f: Int => A)(implicit ev: ArrBuild[A, AA]): AA =
  { val iLen = (iTo - iFrom + iStep).max(0) / iStep
    val res: AA = ev.newArr(iLen)
    var index = 0
    iToForeach(iFrom, iTo, iStep){ count => ev.arrSet(res, index, f(count)); index += 1  }
    res
  }

  /** Maps a range of Ints to an ArrImut[A]. From the start value until (while index is less than) the end value in integer steps. Default step value
   *  is 1. Throws on non termination. */
  def iUntilMap[A, AA <: ArrBase[A]](iFrom: Int, iUntil: Int, iStep: Int = 1)(f: Int => A)(implicit ev: ArrBuild[A, AA]): AA =
    iToMap[A, AA](iFrom, ife(iStep > 0, iUntil - 1, iUntil + 1), iStep)(f)

  /** flatMaps over a range of Ints to an ArrBase[A]. From the start value to (while index is less than or equal to) the end value in integer steps.
   *  Default step value is 1. Throws on non termination. */
  def iToFlatMap[AA <: ArrBase[_]](iFrom: Int, iTo: Int, iStep: Int = 1)(f: Int => AA)(implicit ev: ArrFlatBuild[AA]): AA =
  { val buff = ev.newBuff()
    iToForeach(iFrom, iTo, iStep){ i => ev.buffGrowArr(buff, f(i)) }
    ev.buffToArr(buff)
  }

  /** Folds over a range of Ints to an Int. From the start value to (while index is less than or equal to) the end value in integer steps. Default
   *  step value is 1. Throws on non termination. */
  def iToFoldInt(iFrom: Int, iTo: Int, iStep: Int = 1, accInit: Int = 0)(f: (Int, Int) => Int): Int =
  { var acc = accInit
    iToForeach(iFrom, iTo, iStep){ i => acc = f(acc, i) }
    acc
  }

  /** Folds over a range of Ints to an Int. From the start value until (while index is less than) the end value in integer steps. Default step value
   *  is 1. */
  def iUntilFoldInt(iFrom: Int, iUntil: Int, iStep: Int = 1, accInit: Int = 0)(f: (Int, Int) => Int): Int =
    iToFoldInt(iFrom, ife(iStep > 0, iUntil - 1, iUntil + 1), iStep, accInit)(f)

  /** 2 dimensional from-to-step foreach loop. Throws on non terminaton. */
  def ijToForeach(iFrom: Int, iTo: Int, iStep: Int = 1)(jFrom: Int, jTo: Int, jStep: Int = 1)(f: (Int, Int) => Unit): Unit =
    iToForeach(iFrom, iTo, iStep){ i => iToForeach(jFrom, jTo, jStep){ j => f(i, j)}}

  def ijUntilForeach(iFrom: Int, iUntil: Int, iStep: Int = 1)(jFrom: Int, jUntil: Int, jStep: Int = 1)(f: (Int, Int) => Unit): Unit =
    ijToForeach(iFrom, ife(iStep > 0, iUntil - 1, iUntil + 1), iStep)(jFrom, ife(iStep > 0, jUntil - 1, jUntil + 1), jStep)(f)

  /** 2 dimensional map function.  i is the index for the outer loop. j is the index for the inner loop. maps over 2 ranges of Ints to an ArrBase[A].
   * From the start value to (while index is less than or equal to) the end value in integer steps. Default step values are 1. */
  def ijToMap[A, AA <: ArrBase[A]](iFrom: Int, iTo: Int, iStep: Int = 1)(jFrom: Int, jTo: Int, jStep: Int = 1)(f: (Int, Int) => A)
    (implicit ev: ArrBuild[A, AA]): AA =
  { val iLen = (iTo - iFrom + iStep).max(0) / iStep
    val jLen = (jTo - jFrom + jStep).max(0) / jStep
    val arrLen = iLen * jLen
    val res = ev.newArr(arrLen)
    var arrIndex = 0

    ijToForeach(iFrom, iTo, iStep)(jFrom, jTo, jStep){ (i, j) =>
      ev.arrSet(res, arrIndex, f(i, j))
      arrIndex += 1
    }
    res
  }

  /** 2 dimensional map function.  i is the index for the outer loop. j is the index for the inner loop. maps over 2 ranges of Ints to an ArrBase[A].
   * From the start value until (while index is less than) the end value in integer steps. Default step values are 1. */
  def ijUntilMap[A, AA <: ArrBase[A]](iFrom: Int, iUntil: Int, iStep: Int = 1)(jFrom: Int, jUntil: Int, jStep: Int = 1)(f: (Int, Int) => A)
    (implicit ev: ArrBuild[A, AA]): AA = ijToMap[A, AA](iFrom, ife(iStep > 0, iUntil - 1, iUntil + 1), iStep)(jFrom, jUntil - 1, jStep)(f)

  /** ijToMap where i and j have identical ranges. */
  def ijSameToMap[A, AA <: ArrBase[A]](nFrom: Int, nTo: Int, nStep: Int = 1)(f: (Int, Int) => A)(implicit ev: ArrBuild[A, AA]): AA =
    ijToMap[A, AA](nFrom, nTo, nStep)(nFrom, nTo, nStep)(f)

  /** 3 dimensional from-to-step foreach loop. Throws on non termination. */
  def ijkToForeach(iFrom: Int, iTo: Int, iStep: Int = 1)(jFrom: Int, jTo: Int, jStep: Int = 1)(kFrom: Int, kTo: Int, kStep: Int = 1)
    (f: (Int, Int, Int) => Unit): Unit =
    iToForeach(iFrom, iTo, iStep){ i => iToForeach(jFrom, jTo, jStep){ j => iToForeach(kFrom, kTo, kStep){ k => f(i, j, k) } } }

  /** 2 dimensional map function.  i is the index for the outer loop. j is the index for the inner loop. maps over 2 ranges of Ints to an ArrBase[A].
   * From the start value to (while index is less than or equal to) the end value in integer steps. Default step values are 1. */
  def ijkToMap[A, AA <: ArrBase[A]](iFrom: Int, iTo: Int, iStep: Int = 1)(jFrom: Int, jTo: Int, jStep: Int = 1)(kFrom: Int, kTo: Int, kStep: Int = 1)
    (f: (Int, Int, Int) => A)(implicit ev: ArrBuild[A, AA]): AA =
  { val iLen = (iTo - iFrom + iStep).max(0) / iStep
    val jLen = (jTo - jFrom + jStep).max(0) / jStep
    val kLen = (kTo - kFrom + kStep).max(0) / jStep
    val arrLen = iLen * jLen * kLen
    val res = ev.newArr(arrLen)
    var arrIndex = 0

    ijkToForeach(iFrom, iTo, iStep)(jFrom, jTo, jStep)(kFrom, kTo, kStep){ (i, j, k) =>
      ev.arrSet(res, arrIndex, f(i, j, k))
      arrIndex += 1
    }
    res
  }

  implicit class ArrayBufferDoubleExtensions(thisBuff: Buff[Double])
  { def app2(prod: ProdDbl2): Unit = {thisBuff.append(prod._1); thisBuff.append(prod._2)}
  }

  /* This needs to be removed. */
  implicit class FunitRichImp(fu: () => Unit)
  { def +(operand: () => Unit): () => Unit = () => {fu() ; operand()}
  }

  implicit class Tuple2Implicit[A1, A2](thisTuple: Tuple2[A1, A2])
  {
    def bimap[B1, B2](f1: A1 => B1, f2: A2 => B2): Tuple2[B1, B2] = (f1(thisTuple._1), f2(thisTuple._2))
    def f2[B](f: (A1, A2) => B): B = f(thisTuple._1, thisTuple._2)
  }

  implicit class EqerImplicit[T](thisT: T)(implicit ev: Eq[T])
  { def equ(operand: T): Boolean = ev.eqv(thisT, operand)
    def nequ(operand: T): Boolean = !equ(operand)
  }

  /** Needs Changing. */
  implicit class RefBufferExtensions[A <: AnyRef](thisBuff: Buff[A])
  { @inline def toRefs(implicit ct: ClassTag[A]): Arr[A] = new Arr[A](thisBuff.toArray[A])
    def goodRefs(implicit ct: ClassTag[A]): Good[Arr[A]] = Good(new Arr(thisBuff.toArray))

    def toReverseRefs(implicit ct: ClassTag[A]): Arr[A] =
    { val len = thisBuff.length
      val acc: Array[A] = new Array[A](len)
      var count = 0

      while (count < len)
      { acc(count) = thisBuff(len - 1 - count)
        count += 1
      }
      new Arr(acc)
    }
  }

  implicit class IterableAnyRefImplicit[A <: AnyRef](thisIter: Iterable[A])(implicit ct: ClassTag[A])
  { /** Converts this Iterable to an immutable Array, Arr. */
    def toArr: Arr[A] =
    { val buff: Buff[A] = Buff()
      thisIter.foreach(buff.append)
      buff.toRefs
    }
  }

  implicit def AnyTypeToExtensions[T](thisT: T): AnyTypeExtensions[T] = new AnyTypeExtensions[T](thisT)
  implicit def arrayToExtensions[A](arr: Array[A]): ArrayExtensions[A] = new ArrayExtensions[A](arr)
  implicit def booleanToExtensions(b: Boolean): BooleanExtensions = new BooleanExtensions(b)
  implicit def doubleToExtensions(d: Double): DoubleImplicit = new DoubleImplicit(d)
  implicit def intToExtensions(i: Int): IntExtensions = new IntExtensions(i)
  implicit def iterableToExtensions[A](iter: Iterable[A]): IterableExtensions[A] = new IterableExtensions[A](iter)
  implicit def listToExtensions[A](thisList: List[A]): ListExtensions[A] = new ListExtensions[A](thisList)

  implicit def charToExtensions(thisChar: Char): CharExtensions = new CharExtensions(thisChar)
  implicit def longToImplicit(i: Long): LongExtensions = new LongExtensions(i)
  implicit def optionToExtension[A](thisOption: Option[A]): OptionExtensions[A] = new OptionExtensions(thisOption)

  implicit def seqToExtensions[A](thisSeq: Seq[A]): SeqExtensions[A] = new SeqExtensions(thisSeq)
  implicit def showTypeToExtensions[A](thisVal: A)(implicit ev: Show[A]): ShowerTypeExtensions[A] = new ShowerTypeExtensions[A](ev, thisVal)
  implicit def show2TypeToExtensions[A1, A2,  T](thisVal: T)(implicit ev: Show2[A1, A2, T]): Show2erTypeExtensions[A1, A2, T] =
    new Show2erTypeExtensions[A1, A2, T](ev, thisVal)

  implicit def stringToExtensions(s: String): StringImplicit = new StringImplicit(s)
  implicit def stringIterableToExtensions(strIter: Iterable[String]): StringIterableExtensions = StringIterableExtensions(strIter)
  implicit def stringArrayToExtensions(strArray: Array[String]): StringIterableExtensions = StringIterableExtensions(strArray)
}