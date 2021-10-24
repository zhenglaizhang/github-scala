object p10 extends S99Data {
  def encode[A](xs: List[A]): List[(Int, A)] = {
    if (xs.isEmpty) Nil
    else {
      val (packed, next) = xs.span(xs.head == _)
      (packed.length -> packed.head) :: encode(next)
    }
  }

  def main(args: Array[String]): Unit = {
    val r = encode(symbolList)
    println(r)
    assert(r ==
      List((4, Symbol("a")), (1, Symbol("b")), (2, Symbol("c")), (2, Symbol("a")), (1, Symbol("d")), (4, Symbol("e"))))
  }
}
