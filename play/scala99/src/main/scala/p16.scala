package net.zhenglai

// TODO: fixme
object p16 extends S99Data {
  def drop[A](n: Int, xs: List[A]): List[A] = {
    xs.take(n - 1) ++: drop(n, xs.drop(n))
  }

  def main(args: Array[String]): Unit = {
    val r = drop(3, charList)
    println(r)
    assert(r == List('a', 'b', 'd', 'e', 'g', 'h', 'j', 'k'))
  }
}
