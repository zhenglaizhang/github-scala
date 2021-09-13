package net.zhenglai
package basic

// foreach
// map
// flatMap
// withFilter

@main
def forcomp1() = {
  val states = Vector("Hello", "WORLd", "sCala")
  val r1 = for
    s <- states
    c <- s
    if c.isLower
    c2 = s"$c-${c.toUpper}"
  yield (c, c2)
  println(r1)

  val r2 = states.flatMap(_.toSeq)
    .withFilter(_.isLower)
    .map(c => (c, s"$c-${c.toUpper}"))
  println(r2)
  assert(r1 == r2)
}


@main def pat() = {
  val z @ (x, y) = 1 -> 2
  println ( z )
  println ( x )
  println ( y )
}

trait WithFilter[A, M[_]] {
  def map[B](f: A => B): M[B]
  def flatMap[B](f: A => M[B]): M[B]
  def foreach[U](f: A => U): Unit
  def withFilter(q: A => Boolean): WithFilter[A, M]
}

@main def wf() = {
  val xs = Seq(1, 2, 3)
  xs.withFilter(_ > 2).foreach(println)
}

@main def either() = {
  val e: Either[String, Int] = Left("error")
  // left: error
  // right: value

  // Try/Success/Failure, special case where either left is throwable
}

@main def validated() = {

}