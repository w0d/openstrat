/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat

/** Homogeneous Product6[Double, Double, Double, Double, Double, Double]. These are used in ArrHomoDbl6 Array[Double] based collections. */
trait ProdDbl6 extends Any with Product6[Double, Double, Double, Double, Double, Double] with ProdHomo

/** Base trait for Array[Double] base collections of Products of 6 Doubles. */
trait ArrProdDbl6[A <: ProdDbl6] extends Any with ArrProdDblN[A]
{ def productSize: Int = 6
  def newElem(d1: Double, d2: Double, d3: Double, d4: Double, d5: Double, d6: Double): A

  def apply(index: Int): A =
  { val offset = index * 6
    newElem(arrayUnsafe(offset), arrayUnsafe(offset + 1), arrayUnsafe(offset + 2), arrayUnsafe(offset + 3), arrayUnsafe(offset + 4), arrayUnsafe(offset + 5))
  }

  def setElem(index: Int, elem: A): Unit =
  { val offset = index * 6
    arrayUnsafe(offset) = elem._1; arrayUnsafe(offset + 1) = elem._2; arrayUnsafe(offset + 2) = elem._3; arrayUnsafe(offset + 3) = elem._4; arrayUnsafe(offset + 4) = elem._5
    arrayUnsafe(offset + 5) = elem._6
  }

  def head1: Double = arrayUnsafe(0); def head2: Double = arrayUnsafe(1); def head3: Double = arrayUnsafe(2); def head4: Double = arrayUnsafe(3); def head5: Double = arrayUnsafe(4)
  def head6: Double = arrayUnsafe(5)

  //def toArrs: ArrOld[ArrOld[Double]] = mapArrSeq(el => ArrOld(el._1, el._2, el._3, el._4, el._5, el._6))
  def foreachArr(f: Dbls => Unit): Unit = foreach(el => f(Dbls(el._1, el._2, el._3, el._4, el._5, el._6)))
}

abstract class ProdDbl6sCompanion[A <: ProdDbl6, M <: ArrProdDbl6[A]]
{ val factory: Int => M
  def apply(length: Int): M = factory(length)
  
  def apply(elems: A*): M =
  { val length = elems.length
    val res = factory(length)
    var count: Int = 0
      
    while (count < length)
    { val offset = count * 6
      res.arrayUnsafe(offset) = elems(count)._1; res.arrayUnsafe(offset + 1) = elems(count)._2; res.arrayUnsafe(offset + 2) = elems(count)._3
      res.arrayUnsafe(offset + 3) = elems(count)._4; res.arrayUnsafe(offset + 4) = elems(count)._5; res.arrayUnsafe(offset + 5) = elems(count)._6
      count += 1
    }
    res
  }
   
  def doubles(elems: Double*): M =
  { val arrLen: Int = elems.length
    val res = factory(elems.length / 6)
    var count: Int = 0
    while (count < arrLen) { res.arrayUnsafe(count) = elems(count); count += 1 }
    res
  }
   
  def fromList(list: List[A]): M = 
  { val res = factory(list.length)
    var count: Int = 0
    var rem = list
     
    while (count < list.length)
    { val offset = count * 6
      res.arrayUnsafe(offset) = rem.head._1; res.arrayUnsafe(offset +  1) = rem.head._2; res.arrayUnsafe(offset +  2) = rem.head._3; res.arrayUnsafe(offset +  3) = rem.head._4
      res.arrayUnsafe(offset +  4)= rem.head._5; res.arrayUnsafe(offset + 5 )= rem.head._6
      count += 1
      rem = rem.tail
    }
    res
  }
}