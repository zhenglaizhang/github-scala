import scala.annotation.tailrec

object p02 {
  @tailrec
  def penultimate[A](xs: List[A]): A = xs match {
    // case Nil | _ :: Nil => throw new NoSuchElementException("penultimate")
    case h :: _ :: Nil => h
    case _ :: t => penultimate(t)
    case _ => throw new NoSuchElementException("penultimate")
  }

  def penultimate2[A](xs: List[A]): A = {
    if (xs.length < 2) throw new NoSuchElementException("penultimate2")
    else xs.init.last
  }

  def main(args: Array[String]): Unit = {
    val xs = List(1, 1, 2, 3, 5, 8)
    assert(penultimate(xs) == 5)
    assert(penultimate2(xs) == 5)
  }

  // TODO: generic lastNth(...)
  // http://aperiodic.net/phil/scala/s-99/p02.scala
}
