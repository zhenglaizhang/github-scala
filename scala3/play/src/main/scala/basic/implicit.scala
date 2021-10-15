// Exploiting Implicit Ambiguity in Scala
// We can exploit the entire implicit resolution mechanism to prove the existence (or the ambiguity of existence) of
// type relationships at compile time. This was recently made impossible by the design of the new Scala 3 givens
// mechanism
// this technique should probably not be used as it is in real life code. This article is mostly an illustration of
// what’s possible with the Scala compiler
def processList[A, B](xa: List[A], xb: List[B]): List[(A, B)] =
  for {
    a <- xa
    b <- xb
  } yield (a, b)
// Assume this method is critical for our application, but we can’t change it. Assume that we only want this method
// to be applicable for lists of the same type ONLY
def processListV2[A](xa: List[A], xb: List[A]): List[(A, A)] = processList(xa, xb)
// For a variety of reasons, such a solution may not be appropriate, especially in very general library code
// A little-known type in the Scala library =:=[A,B] (also usable infix as A =:= B) which describes the “equality” of
// types A and B. Upon request — i.e. if we require an implicit instance — the compiler can synthesize an instance of
// that type for two equal types
def processListV3[A, B](xa: List[A], xb: List[B])(using A =:= B): List[(A, B)] = processList(xa, xb)
// Gentle Intro: Proving Type Equality
@main def testProcessList(): Unit = {
  processListV3(List(1, 2, 3), List(4, 5))
  // Cannot prove that Int =:= String.
  // processListV3(List(1, 2, 3), List("4", "5"))
}

// Level 2 Problem: Proving Type DifferencePermalink
// We’ll create a synthetic “not equal” type
trait =!=[A, B]

def processDifferentTypes[A, B](xa: List[A], xb: List[B])(implicit ev: A =!= B): List[(A, B)] = processList(xa, xb)
// We can also follow the same approach as the standard library does with =:= and synthesize implicit instances for
// any two types
// We don't care what value we put in here, we only care it exists.
// This implicit def only ensures we can create instances of =!= for any two types
implicit def neq[A, B]: A =!= B = null
// we want our method to not compile if we use the same type of lists, not because the compiler can’t find an
// implicit instance of =!=, but because it finds too many
implicit def generate1[A]: A =!= A = null
implicit def generate2[A]: A =!= A = null
// the compiler tries to synthesize an instance of Int =!= Int. Because we have generate1 and generate2, that will
// trigger a compiler implicit ambiguity, so the code will not compile because the compiler won’t know which implicit
// to inject! This ambiguity is only triggered for two types which are identical.
//
//  Question: why did we need two implicit defs? Wouldn’t we get an ambiguity with just one generate and neq?
//
//Due to how the compiler tries to synthesize implicit arguments, a single generate implicit def is not enough. That
// is because the signatures of generate and neq are different. Not only different, but the generate’s returned type
// is more specific than that of neq, so when the compiler searches for ways to create an instance of =!=, it will
// take the implicit def with the most specific signature, and then stop.
@main def testDiff(): Unit = {
  // ambiguous implicit arguments: both method generate1 and method generate2 match type Int =!= Int of parameter ev
  // of method
  // processDifferentTypes(List(1,2,3), List(4,5))
}

// If you need something like this, there’s probably something in the design of your code that you should change. If
// someone triggers an implicit ambiguity (which you’ve so diligently added by design), 99% chances are they’ll think
// they made a mistake.

// So what should you do instead? Here are some options:
//  - Define type classes. Make them available (e.g. via implicit defs) only for the type combinations you want to
//    support.
//  - Upgrade to Scala 3 and use the NotGiven technique. It’s built-in and much safer.