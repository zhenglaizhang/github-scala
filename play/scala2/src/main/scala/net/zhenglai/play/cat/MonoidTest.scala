package net.zhenglai.play.cat

import net.zhenglai.cat.instances.MonoidOps.addAll
import net.zhenglai.util.Cool.Debuggable

object MonoidTest {
  // import cats._
  // import cats.implicits._
  import cats.Monoid
  import cats.instances.int._
  import cats.instances.option._
  import cats.instances.string._
  def main(args: Array[String]): Unit = {
    (Monoid[String].combine("x", "y")).p()
    (Monoid[String].empty).p()
    (Monoid[Int].combine(1, 2)).p()

    (Monoid[Option[Int]].combine(Some(1), Some(2))).p() // Some(3)
    (Monoid[Option[Int]].combine(Some(1), None)).p() // Some(1)
    (Monoid[Option[Int]].combine(None, None)).p() // None

    import cats.syntax.semigroup._
    (("Hi" |+| "there" |+| Monoid[String].empty)).p()
    (1 |+| 2 |+| Monoid[Int].empty).p()
    val map1 = Map("a" -> 1, "b" -> 2)
    val map2 = Map("b" -> 3, "c" -> 2)
    (map1 |+| map2).p()

    import cats.instances.tuple._
    val tup1 = ("hello", 12, 12)
    val tup2 = ("world", 124, 13)
    (tup1 |+| tup2).p()
    addAll(List(None, Some(1), Some(2))).p()
    addAll(List(1, 2, 3, 4)).p()
  }
}

object MonoidTestDef {

  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }

  trait Monoid[A] extends Semigroup[A] {
    def empty: A
  }

  object Monoid {
    def apply[A](implicit m: Monoid[A]): Monoid[A] = m
  }
}
