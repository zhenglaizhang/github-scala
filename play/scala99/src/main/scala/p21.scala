package net.zhenglai

object p21 extends S99Data {
  def insertAt[A](a: A, n: Int, xs: List[A]): List[A] = xs.splitAt(n) match {
    case (pre, post) => pre ::: a :: post
  }

  def main(args: Array[String]): Unit = {
    val r = insertAt('n', 1, abcdList)
    println(r)
    assert(r == List('a', 'n', 'b', 'c', 'd'))
  }
}
