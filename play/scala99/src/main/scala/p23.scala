package net.zhenglai

import scala.util.Random

object p23 extends S99Data {
  def rangeSelect[A](n: Int, xs: List[A]): List[A] = {
    if (n <= 0 || xs == Nil) Nil
    else {
      val (xs1, a) = p20.removeAt(Random.between(0, xs.length), xs)
      a :: rangeSelect(n - 1, xs1)
    }
  }

  def main(args: Array[String]): Unit = {
    val r = rangeSelect(3, charList)
    println(r)
  }
}
