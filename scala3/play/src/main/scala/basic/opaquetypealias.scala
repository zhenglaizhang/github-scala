package net.zhenglai
package basic

object Accounting:
  opaque type Dollars = Double
  opaque type Percentage = Double

  object Dollars:
    def apply(d: Double): Dollars = d

    extension (d1: Dollars)
      def +(d2: Dollars): Dollars = d1 + d2
      def -(d2: Dollars): Dollars = d1 - d2
      def *(d2: Percentage): Dollars = d1 * d2
      def /(d2: Dollars): Dollars = d1 / d2
      def toDouble = d1
      // opaque type alias limitation
      //  - cannot override toString or equals
      //  - no pattern matching on them
      // override def toString(): String = f"$$$d1%.2f"

  object Percentage:
    def apply(d: Double): Percentage = d

    extension (p1: Percentage)
      def +(p2: Percentage): Percentage = p1 + p2
      def -(p2: Percentage): Percentage = p1 - p2
      def *(d: Percentage): Percentage = p1 * d
      def toDouble: Double = p1


import basic.Accounting.*

case class Salary(gross: Dollars, taxes: Percentage):
  def net: Dollars = gross - (gross * taxes)


@main def matchableAndOP() = {
  object Obj:
    type Arr[A] = Array[A]
    opaque type OArr[A] = Array[A]
    // opaque type OArr[A] <: Matchable = Array[A]


  summon[Obj.Arr[Int] <:< Matchable]
  // summon[Obj.OArr[Int] <:< Matchable]
  // Cannot prove that Obj.OArr[Int] <:< Matchable.
  // OArr is considered abstract outside of Obj
}

@main def testDollars() = {
  import Accounting.*
  // Outside the defining scope, an opaque type is considered abstract
  val d1 = Dollars(1.1831)
  println(d1.toString) // 1.1381
  println(Salary(Dollars(11), Percentage(0.2)).net)

  val p1 = Percentage(1.1831)
  println(d1 == p1)
  // true
  // actually we want false here, a limitation of opaque type alias
}