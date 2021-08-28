package net.zhenglai
package collection

import cats.kernel.CommutativeMonoid
import cats.syntax.foldable._
import cats.syntax.semigroup._

// commutative replicated data types aka. CRDT for eventual consistency
//  - key ops: merging 2 data instances with a result that captures all the information in both instances

// distributed increment-only counter
//  - e.g. count number of visitors to web sites
final case class GCounter[A](counters: Map[String, A] = Map.empty[String, A]) {
  def increment(machine: String, amount: A)(implicit
      ev: CommutativeMonoid[A]
  ): GCounter[A] = {
    val v = amount |+| counters.getOrElse(machine, ev.empty)
    GCounter(counters + (machine → v))
  }
  def merge(that: GCounter[A])(implicit
      ev: BoundedSemiLattice[A]
  ): GCounter[A] =
    GCounter(this.counters ++ that.counters)
  def total(implicit
      ev: CommutativeMonoid[A]
  ): A = counters.values.toList.combineAll
}

// commutative and idempotent
trait BoundedSemiLattice[A] extends CommutativeMonoid[A] {
  def combine(x: A, y: A): A
  def empty: A
}

object BoundedSemiLattice {
  // TODO: how to model non-negative numbers?
  implicit val intInstance: BoundedSemiLattice[Int] =
    new BoundedSemiLattice[Int] {
      override def combine(x: Int, y: Int): Int = x max y
      override def empty: Int = 0
    }

  implicit def setInstance[A](): BoundedSemiLattice[Set[A]] =
    new BoundedSemiLattice[Set[A]] {
      override def combine(x: Set[A], y: Set[A]): Set[A] = x union y
      override def empty: Set[A] = Set.empty[A]
    }
}
