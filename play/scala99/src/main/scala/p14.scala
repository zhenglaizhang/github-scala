object p14 extends S99Data {
  def duplicate[A](xs: List[A]): List[A] = xs.foldRight(List.empty[A]) { (x, acc) =>
    x :: x :: acc
  }

  def main(args: Array[String]): Unit = {
    val r = duplicate(symbolList2)
    println(r)
    assert(r == List(Symbol("a"), Symbol("a"), Symbol("b"), Symbol("b"), Symbol("c"), Symbol("c"), Symbol("c"),
      Symbol("c"), Symbol("d"), Symbol("d")))
  }
}
