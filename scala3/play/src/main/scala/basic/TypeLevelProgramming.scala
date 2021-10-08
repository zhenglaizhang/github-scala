package net.zhenglai
package basic

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
type _3 = Succ[_2]
type _4 = Succ[_3]
type _5 = Succ[_4]
// Succ[Succ[Succ[Succ[Succ[0]]]]]]
// The compiler can represent any number at all in terms of this succession relationship

// 2.Number Comparison as Types
trait <[A <: Nat, B <: Nat]
@main def testTLP(): Unit = {
  val someComparison: _2 < _3 = ???
}

// 3. The Compiler Validates
// We’ll never build instances of “less-than” ourselves, but we’ll make the compiler build implicit instances of “less-than” just for the right types
