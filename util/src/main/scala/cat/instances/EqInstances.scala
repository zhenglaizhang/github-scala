package net.zhenglai
package cat.instances

import cats.instances.long._
import cats.syntax.eq._
import cats.{Eq, Monoid}

import java.util.Date

trait EqInstances {
  implicit val dateEq: Eq[Date] = Eq.instance[Date] { _.getTime === _.getTime }
}

object MonoidOps {
  def addAll[A](values: Seq[A])(implicit monoid: Monoid[A]): A =
    values.foldRight(monoid.empty)(monoid.combine)
}
