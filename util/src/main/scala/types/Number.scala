package net.zhenglai
package types

// similar to scala's Numeric trait
trait Number[A] {
  def plus(x: A, y: A): A
  def minus(x: A, y: A): A
  def divide(x: A, y: Int): A
  def multiply(x: A, y: A): A
  def sqrt(x: A): A
}

object Number {
  implicit object IntNumber extends Number[Int] {
    override def plus(x: Int, y: Int): Int = x + y
    override def minus(x: Int, y: Int): Int = x - y
    override def divide(x: Int, y: Int): Int = x / y
    override def multiply(x: Int, y: Int): Int = x * y
    override def sqrt(x: Int): Int = Math.sqrt(x).toInt
  }

  implicit object DoubleNumber extends Number[Double] {
    override def plus(x: Double, y: Double): Double = x + y
    override def minus(x: Double, y: Double): Double = x - y
    override def divide(x: Double, y: Int): Double = x / y
    override def multiply(x: Double, y: Double): Double = x * y
    override def sqrt(x: Double): Double = Math.sqrt(x)
  }
}
