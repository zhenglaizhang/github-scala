// how to define our own patterns and make our own types compatible with pattern matching.
// We can easily enable pattern matching on a custom data type
//  - Pattern Matching on Any Type
//  - Custom patterns
//    - A powerful property of unapply is that it doesn’t need to be connected to
//      - the companion object of the class in question
//      - the fields of the class
// pattern matching is applicable to some groups of types in the Scala library:
//  - constants
//  - strings
//  - singletons
//  - case classes
//  - tuples (being case classes as well)
//  - some collections (you’ll now understand why)
//  - combinations of the above

// Much like Scala has a special method called apply, 
// which is often used as a factory method to construct instances, 
// it also has a method called unapply which is used to deconstruct instances through pattern matching

// case classes are automatically eligible for pattern matching. 
// That’s because the compiler automatically generates unapply methods in the companion objects of the class

import scala.Option

class Cat(val name: String, val age: Int) {
  def greet(): String = s"Hello, I am $name, my age is $age"
}

object Cat {
  def unapply(cat: Cat): Option[(String, Int)] = 
    if (cat.age < 21) None 
    else Option.apply((cat.name, cat.age))

  }

/*
Note that the name of the pattern, 
the type subject to pattern matching and the values deconstructed 
many not have any connection to each other. 
It’s quite common that the unapply methods be stored in the companion object of the class/trait in question
*/
object CatDrinkStatus {
  def unapply(age: Int): Option[String] = 
      if (age < 21) Some("minor")
      else Some("legally allowed to drink")
}

// matching sequences
object MatchSequence {
  abstract class MyList[+A] {
    def head: A = throw new NoSuchElementException
    def tail: MyList[A] = throw new NoSuchElementException
  }

  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](xs: MyList[A]): Option[Seq[A]] = 
      if (xs == Empty) Some(Seq.empty)
      else unapplySeq(xs.tail).map(restOfSequence => xs.head +: restOfSequence)
    // return type is Option[Seq[A]]
    // The Seq is a marker to the compiler that the pattern may contain zero, one, or more elements
  }
}

// The mechanism behind unapplySeq is similar to that of unapply. 
// Once you write an unapplySeq, you automatically unlock the vararg pattern
@main def testMatchSequence(): Unit = {
  import MatchSequence.*
  val xs: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  val varargCustom = xs match {
    case MyList(1, 2, 3) => "good"
    case MyList(1, _*) => "start with 1"
    case _ => "all others"
  }
  // The Scala standard library is also rich in pattern-matchable structures: most collections are decomposable with unapplySeq, which is why we can easily write complex patterns on lists:
  List(1, 2, 3) match {
    case List(1, 2, 3) => ??? // regular unapplySeq
    case List(1, _*) => ??? // varargs, unlocked by unapplySeq
    case 3 :: List(2, 3) => ??? // case class :: combined with unapplySeq of rest of the List
    case 2 +: List(2, 3) => ??? // special pattern name called +:, combined with unapplySeq
    case List(2, 2) :+ 3 => ??? // same for :+
    case _ => ??? // catch all
  }
}


object ScalaSearch:
  def unapply(s: String): Boolean = s.toLowerCase.contains("scala")

@main def testpmunapply(): Unit = {
  val books = Seq(
    "Programming Scala",
    "JavaScript: The Good parts",
    "Scala Cookbook"
  ).zipWithIndex

  val results = for s <- books yield s match
    case (ScalaSearch(), index) => s"$index: Found Scala"
    case (_, index) => s"$index: No Scala"

    val c = Cat("meow", 11)
    val r = c match {
      case Cat(n, a) => s"Hello $n you age is $a"
      // name of pattern is `Cat`
      // the compiler looks for an unapply method in the Cat object
      // It does not need to be the companion of the class, but it’s often practiced
      // c match => cat instance c is subject to pattern matching, 
      // i.e. is the argument of the unapply method. 
      // The compiler therefore looks for an unapply(Cat) method inside the Cat object
      // Cat(n,a) means that a tuple of 2 might be returned
      // The pattern match tells the compiler to invoke Cat.unapply(daniel) 
      // and if the result is empty, the pattern did not match; if it did, then it will extract the tuple, 
      // take its fields and put them through the resulting expression
    }

    val legalStatus = c.age match {
      case CatDrinkStatus(status) => s"drink status is $status"
      // The compiler invokes CatDrinkStatus.unapply(daniel.age). 
      // Returning a non-empty Option[String] means that the pattern was matched
    }
}
