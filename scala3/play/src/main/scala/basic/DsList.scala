package net.zhenglai
package basic

@main def list() = {
  assert(List.empty[Nothing] == Nil)
}

@main def vector() = {
  val v1 = Vector.empty :+ "hello" :+ "world"
  println(v1)
  val v2 = "hello" +: "world" +: Vector.empty
  println(v2)
  v2.head
  v2.tail

  // inefficient, a copy of the collection has to be constructed if the Seq is not a vector
  Seq(1, 2, 3).toVector
}

@main
def map() = {
  val m = Map("one" -> 1, "two" -> 2)
  m + ("three" -> 3)
  m ++ Seq("four" -> 4)
}