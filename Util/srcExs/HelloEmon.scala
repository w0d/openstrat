package ostrat

/** Test App for [[EMon]]s. */
object HelloEmon extends App
{ println("Welcome to Hello Emon. This will printout the first number but not the second.")
  val mi1: EMon[Int] = "4".findInt
  mi1.forGood(i => println(i.str))//Something happens
  val mi2: EMon[Int] = "2.2".findInt
  mi2.forGood(i => println(i.str))//Nothing happens.
  val i1: Int = mi2.getElse(0)
  println("You asked for " + i1.str)
  val s1: String = mi2.foldErrs("This really is an Int: " + _) (_ => "This is not an Int")
  println(s1)
  var counter: Int = 10
  println("Counter value = " + counter.str)
  mi1.foldDo { counter += _} {errs => println("The counter was not changed.") }
  println("Counter value is now: " + counter.str)
  mi2.foldDo { counter += _ } { errs => println("The counter was not changed.") }
  println("Counter value is now: " + counter.str)
}
