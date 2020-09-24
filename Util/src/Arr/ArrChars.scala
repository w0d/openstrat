/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat

/** Efficient immutable Array based collection for [[Char]]s. */
final class Chars(val array: Array[Char]) extends AnyVal with ArrBase[Char]
{ type ThisT = Chars
  override def typeStr: String = "Chars"
  override def unsafeNew(length: Int): Chars = new Chars(new Array[Char](length))
  override def length: Int = array.length
  override def apply(index: Int): Char = array(index)
  override def unsafeSetElem(i: Int, value: Char): Unit = array(i) = value
  override def unsafeArrayCopy(operand: Array[Char], offset: Int, copyLength: Int): Unit = { array.copyToArray(array, offset, copyLength); () }
  override def fElemStr: Char => String = _.toString

  /** Append another Chars collection. */
  def ++ (op: Chars): Chars =
  { val newArray = new Array[Char](length + op.length)
    array.copyToArray(newArray)
    op.array.copyToArray(newArray, length)
    new Chars(newArray)
  }

  @inline def offsetter(i: Int): CharsOff = new CharsOff(i)
  @inline def offsetter0: CharsOff = new CharsOff(0)
  @inline def offsetter1: CharsOff = new CharsOff(1)
  @inline def offsetter2: CharsOff = new CharsOff(2)
  @inline def offsetter3: CharsOff = new CharsOff(3)
  @inline def mkString: String = array.mkString
}

object Chars
{ def apply(input: Char*): Chars = new Chars(input.toArray)
}

/** Immutable heapless iterator for Char arrays. */
class CharsOff(val offset0: Int) extends AnyVal with ArrBaseOff[Char, Chars]
{
  override def apply(index: Int)(implicit chars: Chars): Char = chars(offset0 + index)
  def str: String = "CharsOff" + offset0.toString.enParenth
  override def toString = str
  def drop(n: Int): CharsOff = new CharsOff(offset0 + n)
  def drop1: CharsOff = new CharsOff(offset1)
  def drop2: CharsOff = new CharsOff(offset2)
  def drop3: CharsOff = new CharsOff(offset3)
  def drop4: CharsOff = new CharsOff(offset4)
  def length(implicit chars: Chars): Int = chars.length - offset0
  def span(p: Char => Boolean)(implicit array: Chars): (Chars, CharsOff) =
  {
    var count = 0
    var continue = true
    while (offset0 + count < array.length & continue)
    {
      if (p(array(offset0 + count))) count += 1
      else continue = false
    }
    val newArray: Array[Char] = new Array[Char](count)
    iUntilForeach(0, count){i =>
      newArray(i) = array(offset0 + i)}
    (new Chars(newArray), drop(count))
  }
  /** Checks condition against head. Returns false if the collection is empty. */
  def ifHead(f: Char => Boolean)(implicit chars: Chars) : Boolean = (chars.length > offset0) &
    f(chars(offset0))
}

object CharsOff
{
  def unapply(inp: CharsOff): Option[Int] = inp match
  { case _ => Some(inp.offset0)
  }
}

/** Extractor for empty immutable heapless iterator for Chars. */
case object CharsOff0 { def unapply(inp: CharsOff)(implicit chars: Chars): Boolean = chars.length - inp.offset0 <= 0 }

/** Extractor object for immutable heapless iterator for Chars with length == 1. */
object CharsOff1
{ /** Extractor for immutable heapless iterator for Chars with length == 1. */
  def unapply(inp: CharsOff)(implicit chars: Chars): Option[Char] = ife(chars.length - inp.offset0 == 1, Some(chars(inp.offset0)), None)
}

/** Extractor object for immutable heapless iterator for Chars with length == 2. */
object CharsOff2
{ /** Extractor for immutable heapless iterator for Chars with length == 2. */
  def unapply(inp: CharsOff)(implicit chars: Chars): Option[(Char, Char)] =
    ife(chars.length - inp.offset0 == 2, Some((chars(inp.offset0), chars(inp.offset1))), None)
}

/** Extractor object for immutable heapless iterator for Chars with length == 3. */
object CharsOff3
{ /** Extractor for immutable heapless iterator for Chars with length == 3. */
  def unapply(inp: CharsOff)(implicit chars: Chars): Option[(Char, Char, Char)] =
    ife(chars.length - inp.offset0 == 3, Some((chars(inp.offset0), chars(inp.offset1), chars(inp.offset2))), None)
}

/** Extractor object for immutable heapless iterator for Chars with length == 4. */
object CharsOff4
{ /** Extractor for immutable heapless iterator for Chars with length == 4. */
  def unapply(inp: CharsOff)(implicit chars: Chars): Option[(Char, Char, Char, Char)] =
    ife(chars.length - inp.offset0 == 4, Some((chars(inp.offset0), chars(inp.offset1), chars(inp.offset2), chars(inp.offset3))), None)
}

/** Extractor object for the first element for immutable heapless iterator for Chars with at length >= 1. Use this when you don't care about the
 *  tail. */
object CharsOffHead
{ /** Extractor for the first element, for immutable heapless iterator for Chars with length >= 1. Use this when you don't care about the tail. */
  def unapply(inp: CharsOff)(implicit chars: Chars): Option[Char] =
  ife(chars.length - inp.offset0 >= 1, Some(chars(inp.offset0)), None)
}

/** Extractor object for the first 2 elements for immutable heapless iterator for Chars with length >= 2. Use this when you don't care about the
 *  tail. */
object CharsOffHead2
{ /** Extractor for the first 2 elements only for immutable heapless iterator for Chars with at least 2 element. Use this when you don't care about
    * the tail. */
  def unapply(inp: CharsOff)(implicit chars: Chars): Option[(Char, Char)] =
    ife(chars.length - inp.offset0 >= 2, Some((chars(inp.offset0), chars(inp.offset1))), None)
}

/** Extractor object for the first 3 elements for immutable heapless iterator for Chars with length >= 3. Use this when you don't care about the
 *  tail. */
object CharsOffHead3
{ /** Extractor for the first 3 elements only for immutable heapless iterator for Chars with at least 3 element. Use this when you don't care about
 * the tail. */
  def unapply(inp: CharsOff)(implicit chars: Chars): Option[(Char, Char, Char)] =
    ife(chars.length - inp.offset0 >= 3, Some((chars(inp.offset0), chars(inp.offset1), chars(inp.offset2))), None)
}

/** Extractor object for the first 3 elements for immutable heapless iterator for Chars with length >= 3. Use this when you don't care about the
 *  tail. */
object CharsOffHead4
{ /** Extractor for the first 3 elements for immutable heapless iterator for Chars with length >= 3. Use this when you don't care about the tail */
  def unapply(inp: CharsOff)(implicit chars: Chars): Option[(Char, Char, Char, Char)] =
    ife(chars.length - inp.offset0 >= 4, Some((chars(inp.offset0), chars(inp.offset1), chars(inp.offset2), chars(inp.offset3))), None)
}

/** Extractor for immutable heapless iterator for Chars with at l element. */
object CharsOff1Tail
{ /** Extractor for immutable heapless iterator for Chars with at l element. */
  def unapply(inp: CharsOff)(implicit chars: Chars): Option[(Char, CharsOff)] =
  ife(chars.length - inp.offset0 >= 1, Some((chars(inp.offset0), inp.drop1)), None)
}

object CharsOff2Tail
{ def unapply(inp: CharsOff)(implicit array: Chars): Option[(Char, Char, CharsOff)] =
    ife(array.length - inp.offset0 >= 2, Some((array(inp.offset0), (array(inp.offset1)), inp.drop2)), None)
}

object CharsOff3Tail
{ def unapply(inp: CharsOff)(implicit array: Chars): Option[(Char, Char, Char, CharsOff)] =
    ife(array.length - inp.offset0 >= 3, Some((array(inp.offset0), array(inp.offset1), array(inp.offset2), inp.drop3)), None)
}

object CharsOff4Tail
{ def unapply(inp: CharsOff)(implicit array: Chars): Option[(Char, Char, Char, Char, CharsOff)] =
  ife(array.length - inp.offset0 >= 4,
    Some((array(inp.offset0), array(inp.offset1), array(inp.offset2), array(inp.offset3), inp.drop4)),
    None)
}