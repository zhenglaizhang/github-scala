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

  import cats.syntax.either._
  def countPositive(nums: List[Int]): Either[String, Int] =
    nums.foldLeft(0.asRight[String]) { (acc, num) =>
      if (num > 0) acc.map(_ + 1)
      else Left("Negative, stopping!")
    }

}
