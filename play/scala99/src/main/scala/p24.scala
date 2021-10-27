package net.zhenglai

object p24 extends S99Data {
  def lotto(n: Int, max: Int): List[Int] = {
    p23.rangeSelect(n, List.range(1, max + 1))
  }

  def main(args: Array[String]): Unit = {
    val r = lotto(6, 49)
    println(r)
  }
}
