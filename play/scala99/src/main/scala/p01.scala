import scala.annotation.tailrec

// P01 (*) Find the last element of a list.
//     Example:
//     scala> last(List(1, 1, 2, 3, 5, 8))
//     res0: Int = 8

// The start of the definition of last should be
//     def last[A](l: List[A]): A = ...
// The `[A]` allows us to handle lists of any type.
object p01 {
  def last[A](xs: List[A]): A =
    if (xs.isEmpty) throw new NoSuchElementException("last")
    else xs.last

  @tailrec
  def last2[A](xs: List[A]): A = xs match {
    case Nil => throw new NoSuchElementException("last2")
    case h :: Nil => h
    case _ :: t => last2(t)
  }

  def main(args: Array[String]): Unit = {
    val xs = List(1, 1, 2, 3, 5, 8)
    assert(last(xs) == 8)
    assert(last2(xs) == 8)
  }
}
