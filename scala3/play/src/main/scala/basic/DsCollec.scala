package net.zhenglai
package basic

@main def flatmap(): Unit = {
  // one to many
  // effective map then flatten, but more efficient
}

@main def filter(): Unit = {
  // one to zero or one
  val m = Map("one" -> 1, "two" -> 2)
  m filter {
    case (k, v) => k.startsWith("t")
  }
}

@main def fold(): Unit = {
  val xs = Seq(1, 2, 3, 4)
  val xs2 = xs.scan(1)(_ + _)
  println(xs2) // List(1,2,4,7,11)

  xs.product
  xs.sum
  xs.mkString
}
