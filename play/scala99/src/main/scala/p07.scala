object p07 {
  // todo
  //  def flatten[A](xs: Any*): List[A] = xs.foldLeft(List.empty[A]) { (acc, x) =>
  //    x match {
  //      case (h: A) :: t => (acc :+ h) ++: flatten(t)
  //      case h :: t => acc ++: flatten(h) ++: flatten(t)
  //      case Nil => acc
  //    }
  //  }
  def flatten(xs: (List[Any@unchecked])): List[Any@unchecked] = xs flatMap {
    case ms: List[_] => flatten(ms)
    case e: Any@unchecked => List(e)
  }

  def main(args: Array[String]): Unit = {
    val xs: List[Any]@unchecked = List(List(1, 1), 2, List(3, List(5, 8)))
    assert(flatten(xs) == List(1, 1, 2, 3, 5, 8))
  }
}
