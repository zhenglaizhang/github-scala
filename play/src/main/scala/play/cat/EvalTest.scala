package net.zhenglai
package play.cat

import cats.Eval

object EvalTest {
  def test1(): Eval[Int] = {
    val ans = for {
      a <- Eval.now { println("calculating a"); 1 }
      b <- Eval.always { println("calculating b"); 2 }
    } yield {
      println("adding a + b")
      a + b
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
