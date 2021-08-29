package net.zhenglai

import cats.MonadError
import cats.data.Writer
import cats.implicits._

package object types {
  type <=[B, A] = A => B
  type Error = RuntimeException
  type ActualResult[B] = B
  type Or[A, B] = Either[A, B]
  type PossibleResult[A] = ActualResult[A] Or Error

  // A is an extremely broad type
  // Better to define ADT for errors in domain
  type ErrorOr[A] = Either[String, A]
  val monadError = MonadError[ErrorOr, String]

  type Logged[A] = Writer[Vector[String], A]

  // PIMP pattern
  implicit class StringExtension(val s: String) extends AnyVal {
    def isAllUpperCase: Boolean = s.forall(_.isUpper)
  }

  implicit class IterableExtension[A](val xs: Iterable[A]) extends AnyVal {
    def saveToDatabase(): Unit = println("save to db")
  }
}
