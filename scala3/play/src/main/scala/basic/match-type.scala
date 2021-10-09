
// Why we need match types?
//  - Solve a very flexible API unification problem
//  - to be able to express methods returning potentially unrelated types (which are dependent on the input types),
//    in a unified way, which can be correctly type-checked at compile time
//  - Why is this different from regular inheritance-based OOP?
//    def returnConstituentPartOf(thing: Any): ConstituentPart = ... // pattern match
//    // you lose the type safety of your API, because the real instance is returned at runtime.
//  - Generics are used for exactly this purpose: to be able to reuse code and logic on potentially unrelated types
def listHead[T](l: List[T]): T = l.head // return type must be T
// On the other hand, our lastComponentOf method allows the compiler to be flexible in terms of the returned type, depending on the type definition
// Expressed this way, we see how we make the connection between argument and return type more loose, but still
// properly covered.

def lastDigitOf(number : BigInt) : Int = (number % 10).toInt
def lastCharOf(string : String) : Char =
  if string.isEmpty then throw new NoSuchElementException("lastCharOf")
  else string.charAt(string.length - 1)
def lastElemOf[A](xs : List[A]) : A =
  if xs.isEmpty then throw new NoSuchElementException("lastElemOf")
  else xs.last
// You’d like to reduce this API to one grand unifying API which can ideally work for all types. Plus, thinking about
// the future, you’d like to be able to extend this logic to other types as well in the future, perhaps some that are
// completely unrelated
// type-level pattern matching
type ConstituentPartOf[T] = T match
  case BigInt => Int
  case String => Char
  case List[t] => t // pattern on types, done at compile time
val number : ConstituentPartOf[BigInt] = 2
val char : ConstituentPartOf[String] = 'a'
val elem : ConstituentPartOf[List[String]] = "Scala"
// Dependent Methods with Match Types
// value level pattern matching
def lastComponentOf[A <: Matchable](thing : A) : ConstituentPartOf[A] = thing match {
  case b : BigInt => (b % 10).toInt
  case s : String => lastCharOf(s)
  case xs : List[?] => lastElemOf(xs)
}
@main def testMatchType() : Unit = {
  println(lastComponentOf(BigInt(12311314))) // 4
  println(lastComponentOf("Scala")) // a
  println(lastComponentOf((1 to 10).toList)) // 10
}

// Match types can be recursive
type LowestLevelPartOf[T] = T match
  case List[t] => LowestLevelPartOf[t]
  case ? => T

val lastElementOfNestedList: LowestLevelPartOf[List[List[List[Int]]]] = 2

// illegal cyclic type reference: alias ... of type AnnonyingMatchType refers back to the type itself
//type AnnonyingMatchType[A] = A match
//  case ? => AnnonyingMatchType[A]

type InfiniteRecursiveType[A] = A match
  case Int => InfiniteRecursiveType[A]

def somem[A]: InfiniteRecursiveType[A] = ???
// Recursion limit exceeded.
//Maybe there is an illegal cyclic reference?
//If that's not the case, you could also try to increase the stacksize using the -Xss JVM option.
//val invalid = somem[Int]


// Limitation for match type
// The utility of match types in dependent methods is conditioned by the exact signature of the dependent method. So
// far, only methods with the signature are allowed
type MyMatchType[A] = A match {
  case ? => A
}
def method[A](argument: A): MyMatchType[A] = ???

