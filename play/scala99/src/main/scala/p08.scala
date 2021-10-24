import scala.annotation.tailrec

object p08 extends S99Data {
  def compress[A](xs: List[A]): List[A] = xs match {
    case h1 :: h2 :: t if h1 == h2 => compress(h2 :: t)
    case h1 :: h2 :: t if h1 != h2 => h1 :: compress(h2 :: t)
    case x => x
  }

  def compressTailRec[A](xs: List[A]): List[A] = {
    @tailrec
    def loopR(xs: List[A], result: List[A]): List[A] = xs match {
      case h :: t => loopR(t.dropWhile(_ == h), h :: result)
      case Nil => result.reverse
    }

    loopR(xs, Nil)
  }

  // TODO: foldRight
  def compressFold[A](xs: List[A]): List[A] = xs.foldRight(List.empty[A]) { (x, acc) =>
    if (acc.isEmpty || acc.head != x) x :: acc
    else acc
  }

  def main(args: Array[String]): Unit = {
    assert(compress(symbolList) == List(Symbol("a"), Symbol("b"), Symbol("c"), Symbol("a"), Symbol("d"), Symbol("e")))
    assert(compressFold(symbolList) == List(Symbol("a"), Symbol("b"), Symbol("c"), Symbol("a"), Symbol("d"), Symbol
    ("e")))
    assert(compressTailRec(symbolList)
      == List(Symbol("a"), Symbol("b"), Symbol("c"), Symbol("a"), Symbol("d"), Symbol("e")))
  }
}
