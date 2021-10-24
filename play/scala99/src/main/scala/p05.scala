import scala.annotation.tailrec

object p05 {
  def reverse[A](xs: List[A]): List[A] = xs match {
    case Nil => Nil
    case h :: t => reverse(t) :+ h
  }

  def reverseTailRec[A](xs: List[A]): List[A] = {
    @tailrec
    def loopR(xs: List[A], result: List[A]): List[A] = xs match {
      case Nil => result
      case h :: t =>
        loopR(t, h :: result)
    }

    loopR(xs, Nil)
  }

  def reverseFold[A](xs: List[A]): List[A] = xs.foldLeft(List.empty[A]) { (acc, x) => x +: acc }

  def main(args: Array[String]): Unit = {
    val xs = List(1, 1, 2, 3, 5, 8)
    println(reverseTailRec(xs))
    println(List(1, 1, 2) :+ 3)
    assert(reverse(xs) == xs.reverse)
    assert(reverseTailRec(xs) == xs.reverse)
    assert(reverseFold(xs) == xs.reverse)
  }
}
