package net.zhenglai

object p20 extends S99Data {
  def removeAt[A](n: Int, xs: List[A]): (List[A], A) = (n, xs) match {
    case (_, Nil) => throw new NoSuchElementException("removeAt")
    case (0, h :: t) => (t, h)
    case (n, h :: t) => {
      val (r, a) = removeAt(n - 1, t)
      (h :: r) -> a
    }
  }

  def main(args: Array[String]): Unit = {
    val r = removeAt(1, abcdList)
    println(r)
    assert(r == ((List('a', 'c', 'd'), 'b')))
  }
}
