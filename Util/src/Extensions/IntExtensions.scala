/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
import math.Pi

/** Extension methods for Int. */
class IntExtensions(val thisInt: Int) extends AnyVal
{
  /** Returns the value or 0, if this Int less than 0. */
  def atLeast0: Int = ife(thisInt > 0, thisInt, 0)

  /** Returns the value or 0, if this Int more than 0. */
  def atMost0: Int = ife(thisInt > 0, thisInt, 0)

  /** Returns true if this Int is even, false if this Int is odd. */
  def isEven: Boolean = thisInt % 2 == 0

  /** Returns true if this Int is even, false if this Int is odd. */
  def isOdd: Boolean = thisInt % 2 != 0

  /** Returns the first lazily evaluated parameter if this Int is 0 else returns the second lazily evaluated parameter. */
  def if0Else[A](vZero: => A, vNonZero: => A): A = if (thisInt == 0) vZero else vNonZero

  /** Returns the first lazily evaluated parameter if this Int is odd else returns the second lazily evaluated parameter. */
  def ifOddElse[A](vOdd: => A, vEven: => A): A = if (thisInt % 2 == 0) vEven else vOdd

  /** Returns the first lazily evaluated parameter if this Int is even else returns the second lazily evaluated parameter. */
  def ifEvenElse[A](vEven: => A, vOdd: => A): A = if (thisInt % 2 == 0) vEven else vOdd

  /** if the predicate is true apply the function to this Int, else return thisInt unmodified. */
  @inline def ifMod(predicate: Boolean, f: Int => Int): Int = if (predicate) f(thisInt) else thisInt

  /** Divides rounding up. 11.divRoundUp(10) == 2; */
  def divRoundUp(operand: Int): Int = (thisInt / operand).ifMod(thisInt % operand > 0, _ + 1)

  def ifSumEven[A](evenVal: => A, oddVal: => A, operand: Int): A = if((thisInt + operand).isEven) evenVal else oddVal
  def ifSumOdd[A](oddVal: => A, evenVal: => A, operand: Int): A = if((thisInt + operand).isOdd) oddVal else evenVal
  def diff(operand: Int): Int = (thisInt - operand).abs
  def div2: Int = thisInt / 2
  def div4: Int = thisInt / 4
  def div2RoundUp: Int = thisInt / 2 + thisInt % 2
  def div2RoundDown: Int = thisInt / 2 - thisInt % 2
  def million: Int = thisInt *             1000000
  def billion: Long = thisInt.toLong *     1000000000L
  def trillion: Long = thisInt.toLong *    1000000000000L
  def quadrillion: Long = thisInt.toLong * 1000000000000000L
  def spaces: String = (1 to thisInt).foldLeft("")((a, b) => a + " ")
  def commaInts(otherInts: Int *): String = otherInts.foldLeft(thisInt.toString)(_ + ", " + _.toString)
  def semicolonInts(otherInts: Int *): String = otherInts.foldLeft(thisInt.toString)(_ + "; " + _.toString)

  /** More useful definition of modulus where a negative number divided by a positive divisor produces a non negative modulus. */
  def %%(divisor: Int): Int =
  { val r = thisInt % divisor
    if (r < 0) divisor + r else r
  }

  def doTimes(f: () => Unit): Unit =
  { var count: Int = 0
    while(count < thisInt) {f(); count += 1}
  }

  /** folds across the Integer range starting with this Int to the given end of range. */
  def foldTo[A](toValue: Int, initialValue: A)(f: (A, Int) => A): A =
  { var count: Int = thisInt
    var acc: A = initialValue
    while(count <= toValue) { acc = f(acc, count); count += 1 }
    acc
  }

  /** folds across the Integer range starting with this Int until the given end of range. */
  def foldUntil[A](untilValue: Int, initialValue: A)(f: (A, Int) => A): A =
  { var count: Int = thisInt
    var acc: A = initialValue
    while(count < untilValue) { acc = f(acc, count); count += 1 }
    acc
  }

  def str2Dig: String = thisInt match
  { case i if (i > 9) || (i < -9) => i.toString
    case i if (i >= 0) => "0" + i.toString
    case i => "-0" + (-i).toString
  }

  /** Increments the value of an integer while that integer does not match condition. Not guaranteed to terminate. */
  def roundUpTo(f : Int => Boolean): Int = thisInt  match
  { case i if f(i) => i
    case i => (i + 1).roundUpTo(f)
  }

  /** Decrements the value of an integer while that integer does not match condition. Not guaranteed to terminate. */
  def roundDownTo(f : Int => Boolean): Int = thisInt match
  { case i if f(i) => i
    case i => (i - 1).roundDownTo(f)
  }

  def roundUpToEven = thisInt.ifEvenElse(thisInt, thisInt + 1)
  def roundDownToEven = thisInt.ifEvenElse(thisInt, thisInt - 1)
  def roundUpToOdd = thisInt.ifEvenElse(thisInt + 1, thisInt)
  def roundDownToOdd = thisInt.ifEvenElse(thisInt - 1, thisInt)
   
  @inline def hexStr: String = thisInt.toHexString.toUpperCase
  @inline def hexStr2: String = ife(hexStr.length == 1, "0" + hexStr, hexStr)
  def isDivBy3: Boolean = thisInt % 3 == 0

  /** Dividing by 4 gives remainder of 0. */
  def div4Rem0: Boolean = thisInt % 4 == 0

  def div4Rem1: Boolean = thisInt %% 4 == 1

  /** Dividing by 4 gives remainder of 2. */
  def div4Rem2: Boolean = thisInt %% 4 == 2

  def div4Rem3: Boolean = thisInt %% 4 == 3
   
  def isDivBy8: Boolean = thisInt % 8 == 0
  def div8Rem2: Boolean = thisInt %% 8 == 2
  def div8Rem4: Boolean = thisInt %% 8 == 4
  def div8Rem6: Boolean = thisInt %% 8 == 6
   
  def tenthsStr(tenths: Int): String =
  { val res = thisInt.abs * tenths.abs
    val mod = res % 10
    val str1 = (mod != 0).ifMod((res / 10).toString)(_ + "." + mod.toString)
    (thisInt < 0 |!& tenths < 0).ifMod(str1)("-" + _)
  }
   
  def hundredthsStr(hundreds: Int): String =
  { val res = thisInt.abs * hundreds.abs
    val rs = (res / 100).toString
    val str1 = res % 100 match
    { case 0 => rs
      case m if m % 10 == 0 => rs.dotAppend((m / 10).toString)
      case m => rs.dotAppend((m / 10).toString) + (m % 10).toString
    }
    (thisInt < 0  |!& hundreds < 0).ifMod(str1)("-" + _)
  }
  /** Only use positive value that won't overflow int) */
  def power(operand: Int): Int = 1.fRepeat(operand)(a => a * thisInt)

  /** Takes this Int as a value in arc degrees and converts it to a value of radians. */
  @inline def degsToRadians: Double = thisInt * Pi / 180.0

  /** Takes this Int as a value in arc degrees and converts it to a value of arc seconds. */
  @inline def degsToSecs: Int = thisInt * 3600

  /** Takes this Int as a value in radians and converts it to a value of arc degrees. */
  @inline def radiansToDegs: Double = thisInt * 180.0 / Pi

  /** Takes this Int as a value in radians and converts it to a value of arc seconds. */
  @inline def radiansToSecs: Double = thisInt * 3600.0 * 180.0 / Pi

  /** Takes this Int as a value in arc seconds and converts it to a value of radians. */
  @inline def secsToRadians = thisInt * Pi / 180.0 / 3600.0

  /** Takes this Int as a value in arc deconds and converts it to a value of arc degrees. */
  @inline def secsToDegs = thisInt / 3600.0
} 
   