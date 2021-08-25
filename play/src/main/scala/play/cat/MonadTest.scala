package net.zhenglai
package play.cat

object MonadDef {
  trait Functor[F[_]] {
    def map[A, B](a: F[A])(f: A => B): F[B]
  }

  trait FlatMap[F[_]] {
    def flatMap[A, B](a: F[A])(f: A => F[B]): F[B]
  }

  trait Applicative[F[_]] extends Functor[F] {
    def pure[A](a: A): F[A]
  }

  trait Monad[F[_]] extends FlatMap[F] with Applicative[F] {
    def map[A, B](a: F[A])(f: A => B): F[B] =
      flatMap(a)(f.andThen(pure))
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
