/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package ostrat
import utest._

object PersistCollectionsTest  extends TestSuite
{ 
  val tests = Tests
  { 
    val l1 = List(-1, -2, -30)
    val l1Comma: String = "-1, -2, -30"
    val l2: List[Int] = List(4, 5, 6)
    val l2Comma: String = "4, 5, 6"
    val ll: List[List[Int]] = List(l1, l2)
    
    "List" -
    { l1.str ==> "Seq[Int](-1; -2; -30)"
      l1.strSemi ==> "-1; -2; -30"
      l1.strComma ==> l1Comma
      l1.strTyped ==> "Seq[Int](-1; -2; -30)"
      l2.strComma ==> l2Comma
      ll.str ==> "Seq[Seq[Int]](" + l1Comma + "; " + l2Comma + ")"
      ::(4, Nil).str ==> "Seq[Int](4)"
    }
    //val s2 = "Seq(1; 2; 3)"
    
    "List2" -
    {
      //"Seq[Int](1; 2; 3)".findType[List[Int]] ==> Good(List(1, 2, 3))
       // "Seq[Int](1; 2; 3)".findType[List[Int]] ==> Good(List(1, 2, 3))

    //  s1.findType[List[Double]] ==> Good(List(1.0, 2, 3))
      //s1.findType[List[Int]] ==> Good(List(1, 2, 3))
      //s1.findType[List[Int]] ==> Good(Seq(1, 2, 3))
     // s1.findType[List[Int]] ==> Good(Vector(1, 2, 3))
      //"Seq()".findType[Nil.type] ==> Good(Nil)
    }
    /*
    val a1: Array[String] = Array("3", "4")    
    val a1Res ="Seq[Str](\"3\"; \"4\")"
    
    'Array -
    {
      a1.str ==> a1Res
    }

    'Arr -
    {
      Arr(-1, -2, -3).str ==> """Seq[Int](-1; -2; -3)"""
    }*/
  }
}