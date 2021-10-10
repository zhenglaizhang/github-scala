package net.zhenglai
package play.cat

import cats.data.Writer
import cats.implicits.{catsSyntaxApplicativeId, catsSyntaxWriterId}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object WriterTest {
  def slowly[A](body: => A): A =
    try body
    finally Thread.sleep(100)

  def factorial(n: Int): Logged[Int] = {
    val ans = slowly {
      if (n == 0) {
        1.pure[Logged]
      } else {
        for {
          x <- n.pure[Logged]
          y <- factorial(x - 1)
        } yield x * y
      }
    }
    ans.mapWritten(x => x.appended(s"fac: ${n} ${ans.value}"))
  }

  type Logged[A] = Writer[Vector[String], A]

  def flatmap(): Unit = {
    val w1 = for {
      a <- 10.pure[Logged]
      _ <- Vector("a", "b", "c").tell
      c <- 32.writer(Vector("x", "y", "c"))
    } yield a + c
    println(w1.run)

    val w2 = w1.mapWritten(_.map((_.toUpperCase)))
    println(w2.run)

    println(
      w2.bimap(
          log => log.map(_.toUpperCase),
          res => res * 10
        )
        .run
    )

    println(w2.mapBoth { (log, res) => (log.map(_.toUpperCase), res * 10) }.run)

    w2.reset // clear the log
    println(w2.swap.run) // swap log and result
  }

  def basic(): Unit = {
    val x = 123.pure[Logged]
    println(x.value)
    println(x.written)
    val y = Vector("msg1", "msg2", "msg3").tell
    println(y.value)
    println(y.written)
    val (log, result) = y.run
    println(log)
    println(result)

  }

  def main(args: Array[String]): Unit = {
//    basic()
//    flatmap()
    val r = Await.result(
      Future.sequence(
        Vector(
          Future(factorial(5)),
          Future(factorial(5))
        )
      ),
      1.second
    )
    println(r)
  }
}
