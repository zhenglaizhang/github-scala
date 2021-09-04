package net.zhenglai
package util

import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.semigroup._
import cats.{Monad, Monoid, Semigroupal}

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Cool {
  implicit def intsToString(xs: List[Int]): String =
    xs.map(_.toChar).mkString

  implicit class Debuggable[A](a: A) {
    def p(): Unit = println(a)
  }

  implicit class FunChainable[A](a: A) {
    def pipe[B](f: A => B): B = f(a)
    def |>[B](f: A => B): B = pipe(f)
  }

  def concatString[F[_]: Monad](xs1: F[String], xs2: F[String]): F[String] =
    for {
      x <- xs1
      y <- xs2
    } yield x + y

  def balancedFold[A, B](xs: IndexedSeq[A])(fn: A => B)(implicit
      m: Monoid[B]
  ): B = {
    if (xs.isEmpty) { m.empty }
    else if (xs.length == 1) {
      fn(xs.head)
    } else {
      val (l, r) = xs.splitAt(xs.length / 2)
      m.combine(balancedFold(l)(fn), balancedFold(r)(fn))
    }
  }

  def foldMap[A, B: Monoid](xs: Seq[A])(func: A => B): B =
    // xs.map(f).foldLeft(Monoid[B].empty)(Monoid[B].combine)
    // xs.map(f).foldLeft(Monoid[B].empty)(_ |+| _)
    xs.foldLeft(Monoid[B].empty)(_ |+| func(_))

  def parallelFoldMap[A, B: Monoid](
      xs: Vector[A]
  )(func: A => B): Future[B] = {
    Semigroupal
    import cats.instances.vector._
    import cats.syntax.foldable._
    import cats.syntax.traverse._
    val numCores = Runtime.getRuntime.availableProcessors()
    val groupSize = (1.0 * xs.size / numCores).ceil.toInt
    xs.grouped(groupSize)
      .toVector
      .traverse(group => Future(group.foldMap(func)))
      .map(_.combineAll)
  }

  def stackDepth: Int = Thread.currentThread().getStackTrace.length

  def loopM[M[_]: Monad](m: M[Int], count: Int): M[Int] = {
    println(s"Stack depth: $stackDepth")
    count match {
      case 0 => m
      case n => m.flatMap { _ => loopM(m, n - 1) }
    }
  }

  def insertionSort[A: Ordering](xs: List[A]): List[A] =
    xs.foldLeft(List.empty[A]) { (r, c) =>
      val ord = implicitly[Ordering[A]]
      val (front, back) = r.partition(ord.lt(c, _))
      front ::: c :: back
    }

  def factorial(i: Int): BigInt = {
    // compiler will convert tail call optimization to loop
    @tailrec
    def fact(i: Int, acc: BigInt): BigInt = {
      if (i <= 1) {
        acc
      } else {
        // tail recursive
        fact(i - 1, acc * i)
      }
    }
    fact(i, BigInt(1))
  }
}
