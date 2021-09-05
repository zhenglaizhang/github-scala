package net.zhenglai
package util

object Math {
  def squareRoot: PartialFunction[Int, Double] = {
    case a if a >= 0 => java.lang.Math.sqrt(a)
  }

  object Mean {
    def calc(ds: Seq[Double]): Double = ds.sum / ds.size

    // have same type after erasure: (ds: Seq): Double
    // repeated parameters are implemented with Seq[Double]
    // def calc(ds: Double*): Double = calc(ds)
    def calc(d: Double, ds: Double*): Double = calc(d +: ds)
  }
}
