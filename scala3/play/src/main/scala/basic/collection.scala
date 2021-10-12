package net.zhenglai
package basic

// Many critical Scala collections are not just “functional”, but they are actual functions

object `functional-collection` {
  // The implementation of sets — be it tree-sets, hash-sets, bit-sets
  object set {
    val s = Set(1, 2, 3, 4, 5, 5, 2)

    def main(args: Array[String]): Unit = {
      // apply method makes the set “callable” like a function
      // a set behaves like a function A => Boolean
      // sets ARE functions
      assert(s.contains(2) == s(2))
    }
  }
}

trait RSet[A] extends (A => Boolean) {
  def contains(a: A): Boolean

  def apply(a: A): Boolean = contains(a)

  def +(a: A): RSet[A]

  def -(a: A): RSet[A]
}

object RSet {
  def apply[A](values: A*): RSet[A] =
    values.foldLeft[RSet[A]](new REmpty[A])(_ + _)
}

case class REmpty[A]() extends RSet[A] {
  override def contains(a: A): Boolean = false

  // A single-element set is a property-based set, where the property only returns true for that particular element.
  def +(a: A): RSet[A] = new PBSet[A](_ == a)

  def -(a: A): RSet[A] = this
}

// Pure sets in mathematics are described by their properties. Some sets may be finite, or infinite, some may be
// countable (or not).
case class PBSet[A](property: A => Boolean) extends RSet[A] {
  override def contains(a: A): Boolean = property(a)

  // adding an element means adjusting the property so that it also holds true for the element we want to add
  override def +(a: A): RSet[A] = new PBSet[A](e => property(e) || a == e)

  // removing an element means adjusting the property so that it definitely returns false for the element we’re removing
  override def -(a: A): RSet[A] =
    if (contains(a)) new PBSet[A](e => property(e) && e != a) else this
}

@main def testRSet(): Unit = {
  val r1 = REmpty[Int]() + 1 + 2 + 3 + 4 + 5 + 6 + 6 + 1
  val r2 = RSet(1, 2, 3, 4, 5, 0, 1, 3)
  val r3 = RSet((1 to 10) *)
  assert(r1(42) == false)
  assert(r2(3) == true)
  assert(r3(9) == true)
  println(r2)
  // you can now declare infinite sets, just based on their property.
  val evens = PBSet[Int](_ % 2 == 0)
}

object seq {
  // Sequences are partial functions.  PartialFunction[Int, A]
}

object map {
  // Maps are “invokable” objects on a key, returning a value (if it exists) or throwing an exception (if it doesn’t)
  // PartialFunction[K, V]
}