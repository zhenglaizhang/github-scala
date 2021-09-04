package net.zhenglai
package types

object l {
  sealed trait List[+A]
  case object Nil extends List[Nothing]
  case class Cons[+A](head: A, tail: List[A]) extends List[A]
}

object s {
  // A stream is just a lazily evaluated list
  sealed trait Stream[+A] {}
  case object Empty extends Stream[Nothing]
  case class Cons[+A](head: () => A, tail: () => Stream[A]) extends Stream[A]
  object Stream {
    def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
      lazy val head = hd
      lazy val tail = tl
      Cons(() => head, () => tail)
    }

    def empty[A]: Stream[A] = Empty

    def take[A](n: Int): Stream[A] = ???

    def from(i: Int): Stream[Int] = cons(i, from(i + 1))

    // TODO: foldRight/map/flatMap
  }
}
