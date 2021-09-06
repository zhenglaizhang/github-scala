package net.zhenglai
package types

import scala.annotation.tailrec

package object control {
  // implementation of by name parameter is Function0
  // def continue(nonNegative: => Int)(body: => Unit): Unit = ???
  // => have same type after erasure: (nonNegative: Function0, body: Function0): Unit

  @tailrec
  def continue(cond: => Boolean)(body: => Unit): Unit =
    if (cond) {
      body
      continue(cond)(body)
    }
}
