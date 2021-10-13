package net.zhenglai
package play.cat

import cats.{Monad, MonadError}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

object fundamental {
  import cats.instances.list._
  // List, Options, Try, IO all share the same properties:
  //  - the ability to "construct" an instance of this type out of a plain value
  //  - the ability to transform wrapped values through functions
  //  - the ability to chain computations through functions that take a value and return a new wrapper type
  def main(args: Array[String]): Unit = {
    println(combine(List("a", "b", "c"))(List(1, 2, 3)))
  }

  def combine[M[_]: Monad](str: M[String])(num: M[Int])(implicit
      monad: Monad[M]
  ): M[(String, Int)] = monad.flatMap(str)(s => monad.map(num)(n => (s, n)))
// todo use short for comprehension
//    for {
//      s <- str
//      n <- num
//    } yield (s, n)

}

object MonadErrorTest {
  def main(args: Array[String]): Unit = {
    // MonadError[Either, A]
    type ErrorOr[A] = Either[String, A]
    val monadError = MonadError[ErrorOr, String]
    val success = monadError.pure(1)
    monadError.raiseError("badness")
    monadError.ensure(success)("number too low")(_ > 100)
    import cats.syntax.applicative._
    import cats.syntax.applicativeError._
    1.pure[ErrorOr]
    "badness".raiseError[ErrorOr, Int]

    import cats.syntax.monadError._
    success.ensure("Number too low!")(_ > 10)

    // MonadError[Try, A]
    import cats.instances.try_._
    val exn: Throwable = new RuntimeException("boom")
    exn.raiseError[Try, Int]

    // MonadError[Future, A]
    val x = exn.raiseError[Future, Int]
    println(x)
  }
}

object EitherRightBiased {
  def main(args: Array[String]): Unit = {
    val e1: Either[String, Int] = Right(10)
    val e2: Either[String, Int] = Right(11)
//    val c1 = for {
//      a <- e1.right // deprecated
//      b <- e2.right
//    } yield a + b
    val c2 = for {
      a <- e1
      b <- e2
    } yield a + b
    println(c2)
//    assert(c1 == c2)

    import cats.syntax.either._
    "Error".asLeft[Int].getOrElse(0)
    "Error".asLeft[Int].orElse(2.asRight[String])
    "error".asLeft[Int].recover {
      case _: String => -1
    }
    "error".asLeft[Int].recoverWith {
      case _: String => Right(-1)
    }
    "foo".asLeft[Int].leftMap(_.reverse)
    println(6.asRight[String].bimap(_.reverse, _ * 7))
    println("foo".asLeft[String].bimap(_.reverse, _ * 7))

    123.asRight[String].swap

    1.asRight[String].fold(identity, _.toString)

    Either.catchOnly[NumberFormatException]("foo".toInt)
    Either.catchNonFatal(sys.error("badness"))
    Either.fromTry(Try("foo".toInt))
    Either.fromOption[String, Int](None, "badness")
    val x = Either.unit
    println(x)
  }

}

object MonadTest {
  def stringDivideBy(str1: String, str2: String): Option[Int] =
    for {
      a <- str1.toIntOption
      b <- str2.toIntOption
      ans <- if (b == 0) None else Some(a / b)
    } yield ans

  import cats.instances.option._
  import cats.syntax.applicative._
  import cats.syntax.flatMap._
  import cats.syntax.functor._
  import cats.{Id, Monad}
  def main(args: Array[String]): Unit = {
    val o1 = Monad[Option].pure(3)
    val o2 = Monad[Option].flatMap(o1)(a => Some(a + 2))
    val o3 = Monad[Option].flatMap(o2)(a => Some(100 * a))
    println(o3)

    1.pure[Option]
    1.pure[List]
    1.pure[Id]

    val a = Monad[Id].pure(4)
    val b = Monad[Id].flatMap(a)(_ + 1)
    val c = for {
      x <- a
      y <- b
    } yield a + b
    println(c)
  }

}
