package net.zhenglai

object p15 extends S99Data {
  def duplicateN[A](n: Int, xs: List[A]): List[A] = xs.flatMap { x =>
    List.fill(n)(x)
  }

  def main(args: Array[String]): Unit = {
    val r = duplicateN(3, symbolList2)
    val expected = List(Symbol("a"), Symbol("a"), Symbol("a"), Symbol("b"), Symbol("b"), Symbol("b"), Symbol("c"),
      Symbol("c"), Symbol("c"), Symbol("c"), Symbol("c"), Symbol("c"), Symbol("d"), Symbol("d"), Symbol("d"))
    assert(r == expected)
  }
}
