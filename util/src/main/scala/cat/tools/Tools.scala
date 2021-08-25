package net.zhenglai
package cat.tools

import cats.Monad
import cats.syntax.flatMap._
import cats.syntax.functor._

object Tools {

  /**
    * e.g sumSquare(Option(3)), Option(4))
    *  sumSquare(List(1, 2, 3), List(4, 5))
    * @param a
    * @param b
    * @tparam F
    * @return
    */
  def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
    for {
      x <- a
      y <- b
    } yield x * x + y * y

  def sumSquare2[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
    a.flatMap(x => b.map(y => x * x + y * y))

}
