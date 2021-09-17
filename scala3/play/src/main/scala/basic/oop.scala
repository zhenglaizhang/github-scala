package net.zhenglai
package basic

import scala.annotation.targetName

trait Good:
  def x(suffix: String): String
  val x: String
  // def x(): String // double definition


class Name1(var value: String)
class Name2(s: String):
  private var _value: String = s
  def value: String = _value
  def value_=(newValue: String): Unit = _value = newValue

class Invariant[A](var mut: A)
// class Covariant[+A](var mut: A)
// covariant type A occurs in contravariant position in type A of parameter mut_=

// class Contravariant[-A](var mut: A)
// contravariant type A occurs in covariant position in type A of variable mut



case class Complex(real: Double, imag: Double):
  @targetName("negate") def unary_- : Complex = Complex(-real, imag)
  @targetName("minus") def -(other: Complex) = Complex(real - other.real, imag - other.imag)

@main def v() = {
  val n = Name2("abc")
  n.value = "cde"
  println(n.value)
  n.value_=("fgh")
  println(n.value)

  val c = Complex(1.1, 2.2)
  println(-c)
}
