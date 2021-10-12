// Monoid
// Monoids are Semigroups with a twist: besides the 2-arg combination function, Monoids also have an “identity”, aka
// a “zero” or “empty” element. The property of this zero element is that combine(zero, x) == x for all x in the set
// (type in our case)

trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

object Monoid {
  def apply[A](using monoid: Monoid[A]) = monoid
}

object MonoidInstances {
  given intMonoid: Monoid[Int] with {
    override def combine(a: Int, b: Int): Int = a + b

    override def empty: Int = 0
  }

  given stringMonoid: Monoid[String] with {
    override def combine(a: String, b: String): String = a + b

    override def empty: String = ""
  }
}

// Since we have two givens for Monoid and two givens for Semigroup, they might come into conflict if we import both
// (because both are also semigroups). Therefore, it’s usually a good idea to organize type class instances per
// supported type instead of per type class.
// Both will serve as both Semigroups or Monoids depending on which type class we require
object IntInstances {
  given intMonoid: Monoid[Int] with {
    override def combine(a: Int, b: Int): Int = a + b

    override def empty: Int = 0
  }

  given stringMonoid: Monoid[String] with {
    override def combine(a: String, b: String): String = a + b

    override def empty: String = ""
  }
}

@main def testMonoid(): Unit = {
  import IntInstances.given
  val im = Monoid[Int]
  assert(im.combine(im.empty, 12) == 12)
  val r1 = im.combine(13, 12)
  println(r1)
}

// Similar with cats
//import cats.Semigroup // similar trait to what we wrote
//import cats.instances.int._ // analogous to our IntInstances import
//val naturalIntSemigroup = Semigroup[Int] // same apply method
//val intCombination = naturalIntSemigroup.combine(2, 46) // same combine method
//
//// once the semigroup is in scope
//import cats.syntax.semigroup._ // analogous to our SemigroupSyntax import
//val anIntSum = 2 |+| 3