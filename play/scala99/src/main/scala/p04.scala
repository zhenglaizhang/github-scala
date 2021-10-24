import scala.annotation.tailrec

object p04 {
  def length[A](xs: List[A]): Int = xs match {
    case Nil => 0
    case _ :: t => 1 + length(t)
  }

  def lengthTailRecursive[A](xs: List[A]): Int = {
    @tailrec
    def loopR(n: Int, l: List[A]): Int = l match {
      case Nil => n
      case _ :: tail => loopR(n + 1, tail)
    }

    loopR(0, xs)
  }

  def main(args: Array[String]): Unit = {
    val xs = List(1, 1, 2, 3, 5, 8)
    assert(length(xs) == 6)
    assert(lengthTailRecursive(xs) == 6)
  }
}
