package net.zhenglai
package types
import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}
import cats.kernel.Semigroup
import cats.syntax.either._
import cats.syntax.semigroup._

object Strategy1 {
  trait Check[E, A] {
    def apply(value: A): Either[E, A]
    def and(that: Check[E, A]): Check[E, A]
  }

  final case class CheckF[E, A](
      func: A => Either[E, A]
  ) /*extends Check[E, A]*/ {
    def apply(a: A): Either[E, A] = func(a)

    def and(that: CheckF[E, A])(implicit
        s: Semigroup[E]
    ): CheckF[E, A] =
      CheckF { a =>
        (this(a), that(a)) match {
          case (Left(e1), Left(e2)) => (e1 |+| e2).asLeft
          case (Left(e), Right(_))  => e.asLeft
          case (Right(_), Left(e))  => e.asLeft
          case (Right(_), Right(_)) => a.asRight
        }
      }
  }

  def testStrategy1(): Unit = {

    val a: CheckF[List[String], Int] = CheckF { v =>
      if (v > 2) v.asRight
      else List("Must be > 2").asLeft
    }

    val b: CheckF[List[String], Int] = CheckF { v =>
      if (v < -2) v.asRight
      else List("Must be < -2").asLeft
    }

    val check: CheckF[List[String], Int] = a and b
    println(check(5))
    println(check(0))
    println(check(-3))
  }
}

// ADT approach

object Strategy2 {
  sealed trait Check[E, A] {
    import Check._

    def and(that: Check[E, A]): Check[E, A] = And(this, that)

    def apply(a: A)(implicit s: Semigroup[E]): Either[E, A] =
      this match {
        case Pure(func) => func(a)
        case And(left, right) =>
          (left(a), right(a)) match {
            case (Left(e1), Left(e2)) => (e1 |+| e2).asLeft
            case (Left(e), Right(_))  => e.asLeft
            case (Right(_), Left(e))  => e.asLeft
            case (Right(_), Right(_)) => a.asRight
          }
      }
  }
  object Check {
    final case class And[E, A](
        left: Check[E, A],
        right: Check[E, A]
    ) extends Check[E, A]

    final case class Pure[E, A](func: A => Either[E, A]) extends Check[E, A]

    def pure[E, A](f: A => Either[E, A]): Check[E, A] = Pure(f)
  }

  def testCheck(): Unit = {
    val a: Check[List[String], Int] = Check.pure { v =>
      if (v > 2) v.asRight
      else List("Must be > 2").asLeft
    }

    val b: Check[List[String], Int] = Check.pure { v =>
      if (v < -2) v.asRight
      else List("Must be < -2").asLeft
    }

    val check: Check[List[String], Int] = a and b and a
    println(check(1))
    println(check(5))
    println(check(-3))
  }

}

sealed trait Predicate[E, A] {
  import Predicate._
  import cats.syntax.apply._

  def and(that: Predicate[E, A]): Predicate[E, A] = And(this, that)

  def or(that: Predicate[E, A]): Predicate[E, A] = Or(this, that)

  def apply(a: A)(implicit s: Semigroup[E]): Validated[E, A] =
    this match {
      case Pure(func) => func(a)
      case And(left, right) =>
        (left.apply(a), right.apply(a)).mapN((_, _) => a)
      case Or(left, right) =>
        left(a) match {
          case Valid(a) => Valid(a)
          case Invalid(e1) =>
            right(a) match {
              case Valid(a)    => Valid(a)
              case Invalid(e2) => Invalid(e1 |+| e2)
            }
        }
    }
}
object Predicate {
  import cats.syntax.validated._
  final case class And[E, A](
      left: Predicate[E, A],
      right: Predicate[E, A]
  ) extends Predicate[E, A]

  final case class Or[E, A](left: Predicate[E, A], right: Predicate[E, A])
      extends Predicate[E, A]

  final case class Pure[E, A](func: A => Validated[E, A])
      extends Predicate[E, A]

  def apply[E, A](f: A => Validated[E, A]): Predicate[E, A] = Pure(f)

  def lift[E, A](err: E, fn: A => Boolean): Predicate[E, A] =
    Pure(a => if (fn(a)) a.valid else err.invalid)

  def pure[E, A](f: A => Validated[E, A]): Predicate[E, A] = Pure(f)
}

sealed trait Check[E, A, B] {

  def apply(in: A)(implicit s: Semigroup[E]): Validated[E, B]
}

// TODO: complete it
// TODO: support error location
object Check {}

object Test {
  def main(args: Array[String]): Unit = {}
}
