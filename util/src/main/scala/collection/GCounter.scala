package net.zhenglai
package collection

// commutative replicated data types aka. CRDT for eventual consistency
//  - key ops: merging 2 data instances with a result that captures all the information in both instances

// distributed increment-only counter
//  - e.g. count number of visitors to web sites
final case class GCounter(counters: Map[String, Int]) {
  def increment(machine: String, amount: Int): GCounter = {
    val v = amount + counters.getOrElse(machine, 0)
    GCounter(counters + (machine → v))
  }
  def merge(that: GCounter): GCounter =
    GCounter(this.counters ++ that.counters.map {
      case (k, v) ⇒ k -> (v max this.counters.getOrElse(k, 0))
    })
  def total: Int = counters.values.sum
}
