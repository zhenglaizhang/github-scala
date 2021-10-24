import scala.annotation.tailrec

object p03 {
  @tailrec
  def nth[A](n: Int, xs: List[A]): A = (n, xs) match {
    case (0, h :: _) => h
    case (x, _ :: tail) if x > 0 => nth(x - 1, tail)
    case _ => throw new NoSuchElementException("nth")
  }

  def nth2[A](n: Int, xs: List[A]): A =
    if (n >= 0) xs(n)
    else throw new NoSuchElementException("nth2")

  def main(args: Array[String]): Unit = {
    val xs = List(1, 1, 2, 3, 5, 8)
    assert(nth(2, xs) == 2)
    assert(nth2(2, xs) == 2)
  }
}
