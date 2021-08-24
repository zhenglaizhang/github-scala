package net.zhenglai.play.cat

object MonoidTest {
  // import cats._
  // import cats.implicits._
  import cats.Monoid
  import cats.instances.int._
  import cats.instances.option._
  import cats.instances.string._
  def main(args: Array[String]): Unit = {
    println(Monoid[String].combine("x", "y"))
    println(Monoid[String].empty)
    println(Monoid[Int].combine(1, 2))

    println(Monoid[Option[Int]].combine(Some(1), Some(2))) // Some(3)
    println(Monoid[Option[Int]].combine(Some(1), None)) // Some(1)
    println(Monoid[Option[Int]].combine(None, None)) // None

    import cats.syntax.semigroup._
    println(("Hi" |+| "there" |+| Monoid[String].empty))
    println(1 |+| 2 |+| Monoid[Int].empty)
    val map1 = Map("a" -> 1, "b" -> 2)
    val map2 = Map("b" -> 3, "c" -> 2)
    println(map1 |+| map2)

    import cats.instances.tuple._
    val tup1 = ("hello", 12, 12)
    val tup2 = ("world", 124, 13)
    println(tup1 |+| tup2)

    import net.zhenglai.cat.instances.MonoidOps._
    println(addAll(List(None, Some(1), Some(2))))
    println(addAll(List(1, 2, 3, 4)))
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
