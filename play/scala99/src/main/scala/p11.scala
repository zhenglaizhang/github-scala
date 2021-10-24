object p11 extends S99Data {
  def encode[A](xs: List[A]): List[Any] = {
    if (xs.isEmpty) Nil
    else {
      val (packed, next) = xs.span(xs.head == _)
      if (next == Nil) {
        if (packed.length == 1) {
          packed
        } else {
          List(packed.length -> packed.head)
        }
      } else {
        if (packed.length == 1) {
          packed.head :: encode(next)
        } else {
          (packed.length -> packed.head) :: encode(next)
        }
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val expected = List((4, Symbol("a")), Symbol("b"), (2, Symbol("c")), (2, Symbol("a")), Symbol("d"), (4, Symbol
    ("e")))
    val result = encode(symbolList)
    assert(result == expected)
  }
}
