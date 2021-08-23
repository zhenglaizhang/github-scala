package net.zhenglai
package cat.instances

import cats.Eq
import cats.instances.long._
import cats.syntax.eq._

import java.util.Date

trait EqInstances {
  implicit val dateEq: Eq[Date] = Eq.instance[Date] { _.getTime === _.getTime }
}
