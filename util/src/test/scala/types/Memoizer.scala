package net.zhenglai
package types
import scala.collection.mutable

// not thread safe
trait Memoizer {
  def memo[A, B](f: A => B): (A => B) = {
    val cache = mutable.Map[A, B]()
    (a: A) ⇒ cache.getOrElseUpdate(a, f(a))
  }
}
