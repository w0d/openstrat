/* Copyright 2018-20 Richard Oliver. Licensed under Apache Licence version 2.0. */
package ostrat
import collection.mutable.ArrayBuffer

/** Homogeneous Product2[Double, Double] with Stringer. These are used in ArrHomoDbl2 Array[Double] based collections. */
trait ProdDbl2 extends Any with Product2[Double, Double] with ProdHomo

/** A Builder trait for companion objects to construct collection classes of Products2[Double, Double]. */
trait ArrProdDbl2Build[A <: ProdDbl2, ArrT <: ArrProdDbl2[A]] extends ArrProdDblNBuild[A, ArrT]
{ type BuffT <: BuffProdDbl2[A]
  final override def elemSize = 2
  override def arrSet(arr: ArrT, index: Int, value: A): Unit = { arr.arrayUnsafe(index * 2) = value._1; arr.arrayUnsafe(index * 2 + 1) = value._2}
  override def buffGrow(buff: BuffT, value: A): Unit = buff.grow(value)// er.append(newElem._1).append(newElem._2).append(newElem._3); () }
}

/** Base trait for Array[Double] base collections of Products of 2 Doubles. */
trait ArrProdDbl2[A <: ProdDbl2] extends Any with ArrProdDblN[A]
{ type ThisT <: ArrProdDbl2[A]

  override def productSize: Int = 2
  /** Method for creating new elements from 2 Doubles. */
  def elemBuilder(d1: Double, d2: Double): A
  def apply(index: Int): A = elemBuilder(arrayUnsafe(2 * index), arrayUnsafe(2 * index + 1))
  def getPair(index: Int): (Double, Double) = (arrayUnsafe(2 * index), arrayUnsafe(2 * index + 1))

  override def unsafeSetElem(index: Int, elem: A): Unit =
  { arrayUnsafe(2 * index) = elem._1
    arrayUnsafe(2 * index + 1) = elem._2
  }
  def head1: Double = arrayUnsafe(0)
  def head2: Double = arrayUnsafe(1)

  def foreachPairTail[U](f: (Double, Double) => U): Unit =
  { var count = 1
    while(count < length) { f(arrayUnsafe(count * 2), arrayUnsafe(count * 2 + 1)); count += 1 }
  }

  def elem1sArray: Array[Double] =
  { val res = new Array[Double](length)
    var count = 0
    while(count < length){ res(count) = arrayUnsafe(count * 2); count += 1 }
    res
  }

  def elem2sArray: Array[Double] =
  { val res = new Array[Double](length)
    var count = 0
    while(count < length){ res(count) = arrayUnsafe(count * 2 + 1); count += 1 }
    res
  }

  /** Functionally appends the operand of type A. This alphanumeric method is not aliased by the ++ operator, to avoid confusion with numeric operators. */
   def append(op: A): ThisT =
    { val newArray = new Array[Double](length + productSize)
      arrayUnsafe.copyToArray(newArray)
      newArray(length) = op._1
      newArray(length + 1) = op._2
      unsafeFromArray(newArray)
    }

  override def foreachArr(f: Dbls => Unit): Unit = foreach(el => f(Dbls(el._1, el._2)))
}

trait ProdDbl2sCompanion[T <: ProdDbl2, ST <: ArrProdDbl2[T]] extends ProdDblNsCompanion[T, ST]
{
  implicit val persistImplicit: ArrProdDbl2Persist[T, ST]
  def prodLen: Int = 2
  //implicit val factory: Int => ST = len => persistImplicit.fromArray(new Array[Double](len * 2))
  def apply(length: Int): ST = factory(length)
  def apply(elems: T*): ST =
  {
    val length = elems.length
    val res = factory(length)
    var count: Int = 0

    while (count < length)
    { res.arrayUnsafe(count * 2) = elems(count)._1
      res.arrayUnsafe(count * 2 + 1) = elems(count)._2
      count += 1
    }
    res
  }

  def fromList(list: List[T]): ST =
  {
    val arrLen: Int = list.length * 2
    val res = factory(list.length)
    var count: Int = 0
    var rem = list

    while (count < arrLen)
    { res.arrayUnsafe(count) = rem.head._1
      count += 1
      res.arrayUnsafe(count) = rem.head._2
      count += 1
      rem = rem.tail
    }
    res
  }
}

/** Both Persists and Builds ProductD2s collection classes. */
abstract class ArrProdDbl2Persist[A <: ProdDbl2, M <: ArrProdDbl2[A]](typeStr: String) extends ArrProdDblNPersist[A, M](typeStr)
{
  override def appendtoBuffer(buf: ArrayBuffer[Double], value: A): Unit =
  { buf += value._1
    buf += value._2
  }

  override def syntaxDepth = 3
  /** Not sure about this implementation. */
  override def showSemi(thisColl: M): String = thisColl.map(el => el._1.str + ", " + el._2.str).mkString("; ")
  override def showComma(thisColl: M): String = show(thisColl)
  //override def show(thisColl: R): String = typeStr - showSemi(thisColl).enParenth
 // override def fromParameterStatements(sts: Refs[Statement]): EMon[M] = ???
 // override def fromClauses(clauses: Refs[Clause]): EMon[M] = ???
}

trait BuffProdDbl2[A <: ProdDbl2] extends Any with BuffProdDblN[A]
{ type ArrT <: ArrProdDbl2[A]
  override def elemSize: Int = 2
  override def grow(newElem: A): Unit = { buffer.append(newElem._1).append(newElem._2); () }
  def dblsToT(d1: Double, d2: Double): A
  def apply(index: Int): A = dblsToT(buffer(index * 2), buffer(index * 2 + 1))
}