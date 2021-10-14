package fp
// ETW
// Monads are types which take values and do something interesting to them by following a certain structure
// 99% of the value of monads is in their structure

// This pattern (and its refactor) is very common, and is composed of two things:
//  1. the ability to wrap a value into my (more interesting) type - in OO terms this is just a "constructor"; we call
// this _unit_, or _pure_, or _apply_
//  2. a function that transforms a wrapper into another wrapper (perhaps of another type) in the same style as the
//  above - we usually call this _bind_ or _flatMap_
// When you’re dealing with this extract-transform-wrap pattern (I’ll call this ETW)

// Monads inherently describe sequential ETW computations: extract this, then transform, then wrap. If you want to
// process it further, same thing: extract, transform, wrap

case class SafeValue[+A](private val internalValue: A) {
  def get: A = synchronized {
    internalValue
  }

  def transform[B](transformer: A => SafeValue[B]): SafeValue[B] = synchronized {
    transformer(internalValue)
  }
}

def giveMeSafeValue[A](a: => A) = SafeValue(a)
@main def testSafeValue(): Unit = {
  // unwrap -> transform -> wrap
  val safeStr: SafeValue[String] = giveMeSafeValue("Scala")
  val str = safeStr.get
  val upperStr = str.toUpperCase
  val upperSafeStr = SafeValue(upperStr)
  println(upperSafeStr.get)
  // Instead of dealing with things “sequentially” in this way, we can define the transformation in the safe value
  // class itself:
  val upperSafeStr2 = safeStr.transform(s => SafeValue(s.toUpperCase))
  println(upperSafeStr2.get)
}
@main def testMonad(): Unit = {
  val cartesianProduct = for {
    x <- 1 to 13
    y <- 'A' to 'D'
  } yield (x, y)
}


// the annoying axioms
// Property 1: left-identity, a.k.a. the “if I had it” pattern
//    The ETW pattern is described in a single line: MyThing(x).flatMap(f). You can express that in two ways:
//  1. If you have the x the monad was built with, you want to apply the transformation on it, so you get f(x).
//  2. If you don’t, you need the ETW pattern, so you need MyThing(x).flatMap(f).
// Either way, you get the same thing. For all monads, MyThing(x).flatMap(f) == f(x).

// Property 2: left-associativity, a.k.a. the useless wrapPermalink
// Assume you have MySuperMonad(x). What should happen when you say
//
// MySuperMonad(x).flatMap(x => MySuperMonad(x))
//If it’s not obvious yet, make it concrete:
//  - example with lists: List(x).flatMap(x => List(x)) = ?
//  - example with Futures: Future(x).flatMap(x => Future(x))= ?
//Nothing changes, right? An inherent property of monads is that MySuperMonad(x).flatMap(x => MySuperMonad(x)) is the
// same as MySuperMonad(x).
// Property 3: right-associativity, a.k.a. ETW-ETW
//   This one is critical, and describes the correctness of multiple flatMap chains on monads
//  MyMonad(x).flatMap(f).flatMap(g) == MyMonad(x).flatMap(x => f(x).flatMap(g))
val numbers = List(1, 2, 3)
val incrementer = (x: Int) => List(x, x + 1)
val doubler = (x: Int) => List(x, 2 * x)
// The pattern goes like this:
//  - extract
//  - transform & wrap
//  - extract again
//  - transform & wrap
//which is what the two flatMaps do; in the right hand side we’re compressing the last 3 steps:
//  - extract
//  - transform & wrap & ETW