package net.zhenglai
package util.login

import cats.MonadError

import scala.util.Try

case class User(username: String)

object LoginErrors {
  sealed trait LoginError extends Product with Serializable
  final case class UserNotFound(username: String) extends LoginError
  final case class PasswordIncorrect(username: String) extends LoginError
  case object UnexpectedError extends LoginError

  type ErrorOr[A] = Either[LoginError, A]
  type LoginErrorOr = Either[LoginError, User]
}

object Validation {
  def validateAdult[F[_]](age: Int)(implicit
      me: MonadError[F, Throwable]
  ): F[Int] = {
    if (age < 18) {
      me.raiseError(new RuntimeException("too little"))
    } else {
      me.pure(age)
    }
  }

  def main(args: Array[String]): Unit = {
    println(validateAdult[Try](18)) // Success(18)
    println(
      validateAdult[Try](7)
    ) // Failure(java.lang.RuntimeException: too little)

    type ExceptionOr[A] = Either[Throwable, A]
    println(
      validateAdult[ExceptionOr](-1)
    ) // Left(java.lang.RuntimeException: too little)
  }
}
