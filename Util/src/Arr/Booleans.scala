/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
import scala.collection.mutable.ArrayBuffer

/** An immutable Array based class for [[Boolean]]s. */
class Booleans(val array: Array[Boolean]) extends AnyVal with ArrBase[Boolean]
{ type ThisT = Booleans
  override def typeStr: String = "Booleans"
  override def unsafeNew(length: Int): Booleans = new Booleans(new Array[Boolean](length))
  override def length: Int = array.length
  override def apply(index: Int): Boolean = array(index)
  override def unsafeSetElem(i: Int, value: Boolean): Unit = array(i) = value
  override def unsafeArrayCopy(operand: Array[Boolean], offset: Int, copyLength: Int): Unit = { array.copyToArray(array, offset, copyLength); () }
  override def fElemStr: Boolean => String = _.toString

  def ++ (op: Booleans): Booleans =
  { val newArray = new Array[Boolean](length + op.length)
    array.copyToArray(newArray)
    op.array.copyToArray(newArray, length)
    new Booleans(newArray)
  }
}

object Booleans
{ def apply(input: Boolean*): Booleans = new Booleans(input.toArray)
  def ofLength(length: Int): Booleans = new Booleans(new Array[Boolean](length))
}

object BooleansBuild extends ArrBuild[Boolean, Booleans] with ArrFlatBuild[Booleans]
{ type BuffT = BooleanBuff
  override def newArr(length: Int): Booleans = new Booleans(new Array[Boolean](length))
  override def arrSet(arr: Booleans, index: Int, value: Boolean): Unit = arr.array(index) = value
  override def newBuff(length: Int = 4): BooleanBuff = new BooleanBuff(new ArrayBuffer[Boolean](length))
  override def buffGrow(buff: BooleanBuff, value: Boolean): Unit = buff.unsafeBuff.append(value)
  override def buffGrowArr(buff: BooleanBuff, arr: Booleans): Unit = buff.unsafeBuff.addAll(arr.array)
  override def buffToArr(buff: BooleanBuff): Booleans = new Booleans(buff.unsafeBuff.toArray)
}

class BooleanBuff(val unsafeBuff: ArrayBuffer[Boolean]) extends AnyVal with ArrayLike[Boolean]
{ override def apply(index: Int): Boolean = unsafeBuff(index)
  override def length: Int = unsafeBuff.length
}