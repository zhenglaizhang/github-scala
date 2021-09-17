package net.zhenglai
package basic

object matchable {

  // “IArray is considered an abstract type by the compiler. Abstract types are not bounded by Matchable, which is
  // why we now get the warning we want.”
  // Excerpt From: Dean Wampler. “Programming Scala, 3rd Edition.” Apple Books.
  def matchArray() = {
    val iarray = IArray(1, 2, 3, 4, 5)
//    iarray match {
//      case a: Array[Int] => a(2) = 300
    // pattern selector should be an instance of Matchable,,
    // but it has unmatchable type IArray[Int] instead
//    }
    iarray
  }

  // pattern matching can only occur on values of type Matchable, not Any

  def examine[A <: Matchable](xs: Seq[A]): Seq[String] =
    xs map {
      case i: Int => s"Int:$i"
      case other  => s"Other: $other"
    }

  @main def testmat() = {
    // Seq[Any] wont compile
    val xs: Seq[Matchable] = Seq(1, "two", 3, 4.4)
    val mxs: Seq[String] = examine(xs)
    println(mxs)
  }
}
