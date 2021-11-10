package net.zhenglai

object p26 extends S99Data {
  def flatMapsublist[A, B](xs: List[A])(f: List[A] => List[B]): List[B] =
    xs match {
      case Nil => Nil
      case sublist@(_ :: tail) => f(sublist) ::: flatMapsublist(tail)(f)
    }

  def combinations[A](n: Int, xs: List[A]): List[List[A]] =
    if (n == 0) List(Nil)
    else flatMapsublist(xs) { sl =>
      combinations(n - 1, sl.tail) map {
        sl.head :: _
      }
    }

  def main(args: Array[String]): Unit = {
    val r = Set(1, 2, 3, 4).subsets(3)
    println(r.toSeq)
    println(List(1, 2, 3, 4).combinations(3).toSeq)
    val rs = flatMapsublist(List(1, 2, 3))(_.map(x => List(s"$x${x}", s"$x$x$x")))
    println(rs)
    println(combinations(3, charAToFList))
  }
}
