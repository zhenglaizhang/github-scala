// The goal of Monoids, Semigroups, Monads, Traverse, Foldable and other abstractions in functional programming is not
// so that we can inject more math into an already pretty abstract branch of computer science, but because these
// abstractions can be incredibly useful.
// Many high-level constructs expressed as type classes can help make our API more general, expressive and concise at
// the same time, which is almost impossible without them.

// Semigroup
// A semigroup is defined loosely as a set + a combination function which takes two elements of that set and produces
// a third, still from the set.  We generally express this set as a type, so for our intents and purposes, a
// semigroup is defined on a type

trait Semigroup[A] {
  def combine(a: A, b: A): A
}

object Semigroup {
  def apply[A](using instance: Semigroup[A]): Semigroup[A] = instance
}

object SemigroupInstances {
  given intSemigroup: Semigroup[Int] with
    override def combine(a: Int, b: Int): Int = a + b

  given stringSemigroup: Semigroup[String] with
    override def combine(a: String, b: String): String = a + b
}

object SemigroupSyntax {
  extension[A] (a: A)
    def |+|(b: A)(using semigroup: Semigroup[A]): A = semigroup.combine(a, b)
}

object SemigroupSyntaxScala2 {
  implicit class SemigroupExtension[A](a: A)(implicit semigroup: Semigroup[A]) {
    def |+|(b: A): A = semigroup.combine(a, b)
  }
}

// Semigroup helps in creating generalizable 2-arg combinations under a single mechanism.
def reduce[A](xs: List[A])(using semigroup: Semigroup[A]): A = xs.reduce(semigroup.combine)

import SemigroupSyntax.*
def reduceCompact[A: Semigroup](xs: List[A]) = xs.reduce(_ |+| _)

@main def testSemigroup(): Unit = {
  import SemigroupInstances.given
  val is = Semigroup[Int]
  val ss = Semigroup[String]
  val l = is.combine(2, 20)
  val s = ss.combine("Sca", "la")
  println(l)
  println(s)
  val r1 = reduce(List(1, 2, 3))
  val r2 = reduce(List("Hello", " ", "Scala"))
  println(r1)
  println(r2)
  val r3 = reduceCompact(List(1, 2, 3))
  val r4 = reduceCompact(List("Hello", " ", "Scala"))
  println(r3)
  println(r4)
}
