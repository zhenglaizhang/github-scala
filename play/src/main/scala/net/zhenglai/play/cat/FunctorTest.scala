package net.zhenglai
package net.zhenglai.play.cat

import cats.Functor

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import scala.util.Random

class FutureNotRT {
  val fut1 = {
    val r = new Random(0L)
    val x = Future(r.nextInt())
    for {
      a <- x
      b <- x
    } yield (a, b)
  }
  val fut2 = {
    val r = new Random(0L)
    for {
      a <- Future(r.nextInt())
      b <- Future(r.nextInt())
    } yield (a, b)
  }
  def main(args: Array[String]): Unit = {
    val r1 = Await.result(fut1, 1.second)
    val r2 = Await.result(fut2, 1.second)
    println(r1)
    println(r2)
  }

}

class FunctorTest {
  import cats.instances.function._
  import cats.syntax.functor._

  def doMath[F[_]](start: F[Int])(implicit functor: Functor[F]): F[Int] =
    start.map(_ + 1 * 2)

  def main(args: Array[String]): Unit = {
    val f1: Int => Double = _.toDouble
    val f2: Double => Double = _ * 2
    println((f1 map f2)(1))
    println((f1 andThen f2)(1))
    println(f2(f1(1)))

    val liftedF1 = Functor[Option].lift(f1)
    println(liftedF1(Some(1)))
    println(liftedF1(None))

    Functor[Seq].as(Seq(1, 2, 3), "As")
    // TODO: play other Functor methods

    println(doMath(Option(20)))
    println(doMath(List(1, 2, 3)))
  }
}
