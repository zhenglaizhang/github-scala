package net.zhenglai
package basic

object Accounting:
  opaque type Dollars = Double
  opaque type Perentage = Double

  object Dollars:
    def apply(d: Double): Dollars = d

    extension (d1: Dollars)
      def +(d2: Dollars): Dollars = d1 + d2
      def -(d2: Dollars): Dollars = d1 - d2
      def *(d2: Dollars): Dollars = d1 * d2
      def /(d2: Dollars): Dollars = d1 / d2
      def toDouble = d1
      // opaque type alias limitation
      //  - cannot override toString or equals
      //  - no pattern matching on them
      // override def toString(): String = f"$$$d1%.2f"

  object Percentage:
      def apply(d: Double): Perentage = d


@main def testDollars() = {
  import Accounting.*
  // Outside the defining scope, an opaque type is considered abstract
  val d1 = Dollars(1.1831)
  println(d1.toString) // 1.1381
}