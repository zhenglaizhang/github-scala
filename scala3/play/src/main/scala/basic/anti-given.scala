package antigiven

// Making the compiler exploit the absence of a given instance for enforcing type relationships.
def processLists[A, B](la: List[A], lb: List[B]): List[(A, B)] =
  for {
    a <- la
    b <- lb
  } yield (a, b)
def processListsSameTypeGiven[A, B](xa: List[A], xb: List[B])(using A =:= B): List[(A, B)] =
  processLists(xa, xb)
@main def testAntiGiven(): Unit = {
  // The call works because the compiler can synthesize an instance of =:=[Int, Int]
  val a = processListsSameTypeGiven(List(1), List(2))

  // Cannot prove that Int =:= String.
  // val b = processListsSameTypeGiven(List(1), List("2"))
}



// Proving Type Difference
// How can we make sure that we can only call processList with different type arguments?

import scala.util.NotGiven

// The type NotGiven has special treatment from the compiler. Wherever we require the presence of a NotGiven[T], the
// compiler will successfully synthesize an instance of NotGiven[T] if and only if it cannot find or synthesize a
// given instance of T
// In our case, we must not find or synthesize an instance of A =:= B
def processListsDifferentType[A, B](xa: List[A], xb: List[B])(using NotGiven[A =:= B]): List[(A, B)] =
  processLists(xa, xb)
@main def testNotGiven(): Unit = {
  // no implicit argument of type util.NotGiven[Int =:= Int] was found for parameter x$3 of method
  // processListsDifferentType(List(1), List(2))
  processListsDifferentType(List(1), List("2"))
}


