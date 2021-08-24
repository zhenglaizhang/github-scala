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
