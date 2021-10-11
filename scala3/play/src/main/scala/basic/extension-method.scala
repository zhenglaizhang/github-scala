// type enrichment
// PIMPing
// extension methods

case class Person1(name: String) {
  def greet: String = s"Hi $name"
}

object scala2wayimp {
  implicit class PersonLike(str: String) {
    def greet: String = Person1(str).greet
  }
}

@main def testImplicit(): Unit = {
  import scala2wayimp.*
  "zhenglai".greet
  // compiler rewrite as:
  new PersonLike("zhenglai").greet
}

// In Scala 3, the implicit keyword, although fully supported (for this version), is being replaced:
//  - implicit values and arguments replaced for given/using clauses — see how they compare with implicits and how they
//    work in tandem with implicits
//  - implicit defs (used for conversions) are replaced with explicit conversions
//  - implicit classes are replaced with proper extension methods — the focus of this article

object scala3wayimp {
  extension (str: String)
    def greet: String = Person1(str).greet

  // Much like implicit classes, extension methods can be generic
  sealed abstract class Tree[+A]

  case class Leaf[+A](value: A) extends Tree[A]

  case class Branch[+A](left: Tree[A], right: Tree[A]) extends Tree[A]

  // And we have no access to the source code.
  // On the other hand, we want to add some methods that we normally use on lists
  // try with following extension clause
  extension[A] (tree: Tree[A]) // (using numeric: Numeric[A])
    def filter(predicate: A => Boolean): Boolean = tree match {
      case Leaf(value) => predicate(value)
      case Branch(left, right) => left.filter(predicate) || right.filter(predicate)
    }

    // The method itself can be generic.
    def map[B](f: A => B): Tree[B] = tree match {
      case Leaf(value) => Leaf(f(value))
      case Branch(left, right) => Branch(left.map(f), right.map(f))
    }

    // The using clause might be present in the extension clause, or in the method signature itself
    // Or even in both places, if you’d like.
    def sum(using numeric: Numeric[A]): A = tree match {
      case Leaf(value) => value
      case Branch(left, right) => numeric.plus(left.sum, right.sum)
    }

  def main(args: Array[String]): Unit = {
    println("zhenglai".greet)
    val t = Branch(Leaf(1), Leaf(2))
    println(t.sum)
  }
}


