package net.zhenglai
package types

// TODO: “https://github.com/mpilquist/simulacrum/”
object Stats {
  def mean[A: Number](xs: Vector[A]): A =
    implicitly[Number[A]].divide(
      xs.reduce(implicitly[Number[A]].plus),
      xs.size
    )

  def median[A: Number](xs: Vector[A]): A =
    xs(xs.size / 2)

  def variance[A: Number](xs: Vector[A]): A = {
    val m = mean(xs)
    val sqDiff = xs.map { x =>
      val diff = implicitly[Number[A]].minus(x, m)
      implicitly[Number[A]].multiply(diff, diff)
    }
    mean(sqDiff)
  }

  def stddev[A: Number](xs: Vector[A]): A =
    implicitly[Number[A]].sqrt(variance(xs))

}
