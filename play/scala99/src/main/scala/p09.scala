import scala.annotation.tailrec

object p09 extends S99Data {
  def pack[A](xs: List[A]): List[List[A]] = {
    @tailrec
    def loopR(result: List[List[A]], xs: List[A]): List[List[A]] = xs match {
      case Nil => result
      case h :: t => loopR(result :+ (h :: t.takeWhile(_ == h)), t.dropWhile(_ == h))
    }

    loopR(Nil, xs)
  }

  def packWithSpan[A](xs: List[A]): List[List[A]] = {
    if (xs.isEmpty) Nil
    else {
      val (packed, next) = xs.span(_ == xs.head)
      if (next == Nil) List(packed)
      else packed :: pack(next)
    }
  }

  def main(args: Array[String]): Unit = {
    val result = List(List(Symbol("a"), Symbol("a"), Symbol("a"), Symbol("a")), List(Symbol("b")),
      List(Symbol("c"), Symbol("c")), List(Symbol("a"), Symbol("a")), List(Symbol("d")), List(Symbol("e"),
        Symbol("e"), Symbol("e"), Symbol("e")))
    assert(pack(symbolList) == result)
    assert(packWithSpan(symbolList) == result)
  }
}
