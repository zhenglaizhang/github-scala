package net.zhenglai
package basic

// ref: https://blog.rockthejvm.com/type-level-programming-part-1/#3-the-compiler-validates

// Use the power of the Scala compiler to solve problems for you and to validate properties of types
object TypeLevelProgramming {
  import scala.reflect.runtime.universe.*
  def show[A](value: A)(implicit tag: TypeTag[A]) =
    tag.toString.replace("net.zhenglai.basic.TypeLevelProgramming", "")
}

// for Scala 2.13
// libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

// 1. Numbers as Types
// Declare a type that will represent natural numbers
// We dont use Int but represent the relationship between numbers as a succession
// We have numbers and their succession relationship as type constraints
trait Nat
class _0 extends Nat
class Succ[A <: Nat] extends Nat
type _1 = Succ[_0]
type _2 = Succ[_1]
type _3 = Succ[_2] // = Succ[Succ[Succ[_0]]]
type _4 = Succ[_3]
type _5 = Succ[_4]
// The number 5 is now a type, and we represent it as Succ[Succ[Succ[Succ[Succ[0]]]]]]
// The compiler can represent any number at all in terms of this succession relationship

// 2.Number Comparison as Types
trait <[A <: Nat, B <: Nat]

// 3. The Compiler Validates
// We’ll never build instances of “less-than” ourselves,
// but we’ll make the compiler build implicit instances of “less-than” just for the right types
// Through these instances, we’ll thus validate the existence of the “less-than” type for various numbers, and therefore prove the comparison between numbers
object < {
  def apply[A <: Nat, B <: Nat](implicit lt: <[A, B]): <[A, B] = lt
  // These two implicit methods, used in combination, will have the powerful effect of proving the existence of any <[A,B] type, whenever the “less-than” relationship makes sense
  implicit def ltBasic[B <: Nat]: <[_0, Succ[B]] = new <[_0, Succ[B]] {}
  implicit def indutive[A <: Nat, B <: Nat](implicit lt: <[A, B]): <[Succ[A], Succ[B]] = new <[Succ[A], Succ[B]] {}
  // if the compiler can find and implicit instance of <[A, B], then the compiler will also build an instance of <[Succ[A], Succ[B]]
}

sealed trait <=[A <: Nat, B <: Nat]
object <= {
  def apply[A <: Nat, B <: Nat](implicit lte: <=[A, B]): <=[A, B] = lte
  implicit def lteBasic[B <: Nat]: <=[_0, B] = new <=[_0, B] {}
  implicit def indutive[A <: Nat, B <: Nat](implicit lte: <=[A, B]): <=[Succ[A], Succ[B]] = new <=[Succ[A], Succ[B]]{}
}

@main def testTLP(): Unit = {
  val someComparison: _2 < _3 = ???
}

@main def testLessThan(): Unit = {
  val validCmparison: _2 < _3 = <[_2, _3]
  // val invalidComparison: _3 < _2 = <[_3, _2]
}