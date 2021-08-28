package net.zhenglai
package types
import cats.kernel.Semigroup
import cats.syntax.either._
import cats.syntax.semigroup._

trait Check[E, A] {
  def apply(value: A): Either[E, A]
  def and(that: Check[E, A]): Check[E, A]
}

final case class CheckF[E, A](func: A => Either[E, A]) /*extends Check[E, A]*/ {
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

object Test {
  def main(args: Array[String]): Unit = {
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
