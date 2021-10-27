package net.zhenglai
package play.col

object list {
  // optimal for last-in-first-out (LIFO), stack-like access patterns.
  // implements structural sharing of the tail list.
  // This means that many operations have either a constant memory footprint or no memory footprint at all
  // A list has O(1) prepend and head/tail access.
  // Most other operations are O(n) though; this includes length, append, reverse, and also the index-based lookup of
  // elements.
  // This List class comes with two implementing case classes, scala.Nil and scala.::, that implement the abstract
  // members isEmpty, head, and tail
  def main(args: Array[String]): Unit = {
    val xs: List[Int] = 1 :: 2 :: 3 :: Nil
    // match may not be exhaustive.
    // It would fail on the following input: Nil
    // val x :: xs1 = xs
    val x = xs.head
    val xs1 = xs.tail
    println(x)
    println(xs1.head)
    println(xs1.headOption)
    assert(x == 1)
    assert(xs1 == List(2, 3))
    val y = List(1, 2) ::: List(3, 4)
    assert(y == List(1, 2, 3, 4))
    assert(List.fill(2)(100) == List(100, 100))
    assert(xs.reverse == List(3, 2, 1))
  }
}