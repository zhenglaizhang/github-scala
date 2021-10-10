package net.zhenglai
package play.cat

import cats.Eval

// Eval is a monad that allows us to abstract over different models of evaluation
// similar to Future and fs2.Task

// 2 evaluation models:
//  - eager
//  - lazy
object EvalTest {
  // eager and memoized
  val x = {
    println("Computing x")
    1 + 1
  }
//  x
//  x

  // lazy and not memoized
  def y = {
    println("Computing y")
    1 + 1
  }
  y
  y

  // lazy and memoized
  lazy val z = {
    println("computing z")
    1 + 1
  }
  z
  z

  // defs are lazy and not memoized
  def test1(): Eval[Int] = {
    val ans = for {
      a <- Eval.now { println("calculating a"); 1 }
      b <- Eval.always { println("calculating b"); 2 }
      c <- Eval.later { println("calculating c"); 3 }
    } yield {
      println("adding a + b + c")
      a + b + c
    }
    println(ans.value)
    println(ans.value)
    ans
  }

  def say() = {
    val saying = Eval
      .always { println("step1"); "the cat" }
      .map { str => println("step2"); s"$str sat on" }
      .memoize
      .map { str => println("step3"); s"$str the mat" }
    println(saying.value)
    println(saying.value)
  }

  def main(args: Array[String]): Unit = {
    test1()
    println()
    println()
    say()
  }
}
