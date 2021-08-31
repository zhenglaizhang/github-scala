package net.zhenglai
package util

object Math {
  def squareRoot: PartialFunction[Int, Double] = {
    case a if a >= 0 => java.lang.Math.sqrt(a)
  }
}
