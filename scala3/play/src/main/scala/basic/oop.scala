package net.zhenglai
package basic

trait Good:
  def x(suffix: String): String
  val x: String
  // def x(): String // double definition


class Name1(var value: String)
class Name2(s: String):
  private var _value: String = s
  def value: String = _value
  def value_=(newValue: String): Unit = _value = newValue

@main def v() = {
  val n = Name2("abc")
  n.value = "cde"
  println(n.value)
  n.value_=("fgh")
  println(n.value)
}

