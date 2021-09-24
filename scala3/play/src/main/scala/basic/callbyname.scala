package net.zhenglai
package basic

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

object callbyname {
  // the argument is evaluated before the function is invoked. So before the function call, 2 + 3 becomes 5 and you actually call byValueFunction(5)
  def byValueFunction(x: Long) = {
    println(x)
    println(x)
  }

  // The arrow sign before the Int type means “I’ll take an Int expression literally and I’ll take care to evaluate
  // it if I need to”.
  // what you’re passing is not the value of 2 + 3, but the expression 2 + 3, literally, as it is.
  // . Call-by-expression? Call-literally? Phony call? DETISS (don’t evaluate till I say so)
  def byNameFunction(x: => Long) = {
    // the expression is passed literally and it’s being used twice, then it’s evaluated twice, at different moments.
    println(x)
    println(x)
  }

  @main def test1(): Unit = {
    byValueFunction(System.nanoTime())

    // trick1 - reevaluation
    byNameFunction(System.nanoTime())

    // trick2 - manageable infinity
    def getElem(i: Int): Int = {
      println(s"getting $i")
      i
    }
    val x: LazyList[Int] =
      NonEmpty(getElem(1), NonEmpty(getElem(2), NonEmpty(getElem(3), Empty)))
    println(x.head)

    // trick3 - hold the door
    // The third and most powerful aspect of CBN is that it prevents the computation of the argument so that the expression can be handled in some other way
    val onAttempt: Try[Int] = Try { throw new NullPointerException }
    val f = Future { 1 }
  }

}

// trick 2 - manageable infinity
abstract class LazyList[+A] {
  def head: A
  def tail: LazyList[A]
}

case object Empty extends LazyList[Nothing] {
  override def head: Nothing = throw new NoSuchElementException
  override def tail: LazyList[Nothing] = throw new NoSuchElementException
}

// val parameters may not be call-by-name
/*case*/
class NonEmpty[+A](h: => A, t: => LazyList[A]) extends LazyList[A] {
  override lazy val head = h
  override lazy val tail = t // make sure evaluated exactly once
}

// Trick 3 - hold the door
