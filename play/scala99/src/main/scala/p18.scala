object p18 extends S99Data {
  // TODO: more
  def slice[A](from: Int, to: Int, xs: List[A]): List[A] =
    xs.slice(from, to)

  def main(args: Array[String]): Unit = {
    val r = slice(3, 7, charList)
    println(r)
    assert(r == List('d', 'e', 'f', 'g'))
  }
}
