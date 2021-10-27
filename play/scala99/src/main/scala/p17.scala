package net.zhenglai

// TODO: more
object p17 extends S99Data {
  def split[A](n: Int, xs: List[A]): (List[A], List[A]) = (xs.take(3), xs.drop(3))

  def main(args: Array[String]): Unit = {
    val r: (List[Char], List[Char]) = split(3, charList)
    println(r)
    assert(r == ((List('a', 'b', 'c'), List('d', 'e', 'f', 'g', 'h', 'i', 'j', 'k'))))
  }
}
