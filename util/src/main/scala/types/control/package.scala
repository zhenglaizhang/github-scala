package net.zhenglai
package types

import scala.annotation.tailrec

package object control {
  @tailrec
  def continue(cond: => Boolean)(body: => Unit): Unit =
    if (cond) {
      body
      continue(cond)(body)
    }
}
