package net.zhenglai
package util.login

case class User(username: String)

object LoginErrors {
  sealed trait LoginError extends Product with Serializable
  final case class UserNotFound(username: String) extends LoginError
  final case class PasswordIncorrect(username: String) extends LoginError
  case object UnexpectedError extends LoginError

  type ErrorOr[A] = Either[LoginError, A]
  type LoginErrorOr = Either[LoginError, User]
}
