package net.zhenglai
package util

import cats.Monad
import cats.syntax.flatMap._
import cats.syntax.functor._

object Cool {
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

  def foldMap[A, B](xs: Seq[A])(f: A => B): B = ???
}
