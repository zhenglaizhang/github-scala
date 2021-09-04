package net.zhenglai
package types

import types.s.{Cons, Empty}

import org.scalatest.FunSuite

class StreamTest extends FunSuite {
  import s.Stream._
  test("cons") {
    val s: Cons[Int] = Cons(() => 1, () => Cons(() => 2, () => Empty))
    println(s.head)
    println(s.head())

    val xs = cons(1, cons(2, empty))
    println(xs)
  }
}
