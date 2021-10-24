object p12 extends S99Data {
  def decode[A](xs: List[(Int, A)]): List[A] = xs.flatMap { x =>
    List.fill(x._1)(x._2)
  }

  def main(args: Array[String]): Unit = {
    val r = decode(symbolsEncoded)
    println(r)
    assert(r == symbolList)
  }
}
