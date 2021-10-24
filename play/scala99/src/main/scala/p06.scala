import scala.annotation.tailrec

object p06 {
  @tailrec
  def compareList[A](xs: List[A], ys: List[A]): Boolean = (xs, ys) match {
    case (Nil, Nil) => true
    case (h1 :: t1, h2 :: t2) if h1 == h2 => compareList(t1, t2)
    case _ => false
  }

  def isParlindrome[A](xs: List[A]): Boolean = {
    if (xs.length < 2) {
      true
    }
    else {
      val (l, rtemp) = xs.splitAt(xs.length / 2)
      val r = if (rtemp.length > l.length) rtemp.tail else rtemp
      compareList(l, r.reverse)
    }
  }

  def isParlindromeBuiltin[A](xs: List[A]): Boolean = xs == xs.reverse

  def main(args: Array[String]): Unit = {
    val xs = List(1, 2, 3, 2, 1)
    assert(isParlindrome(xs))
    assert(isParlindromeBuiltin(xs))
  }
}
