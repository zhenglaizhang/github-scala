package basic.givenn

// Scala 3 Givens, Implicits and Intentions
//  Implicit defs were never meant to be used like methods. Therefore, there’s a clear discrepancy between the
// structure of the code (a method) and the intention (a conversion). The new Scala 3 contextual abstractions solve
// this problem by being very clear on the intent:
//  - given/using clauses are used for passing “implicit” arguments
//  - implicit conversions are done by creating instances of Conversion
//  - extension methods have their first-class syntactic structure


// Implicits have demonstrated their use and have been battle-tested in many scenarios. Listing just a few:
//  - Implicits are an essential tool for creating type classes in Scala.
//  - Extending the capabilities of existing types, in expressions such as 20.seconds
//  - Implicits allow the automatic creation of new types and enforcing type relationships between them at compile time
// In Scala 2, this suite of capabilities is available under the same implicit keyword, through implicit vals,
// implicit defs and implicit classes.
//  - When we have some working code using implicits, it’s often very hard — and exponentially harder with a growing
//  codebase — to pinpoint which implicits made it possible.
//  - When we write code that requires implicits, we often need to import the right implicits to make it compile.
//  Automatic imports are really hard
//  - Implicit defs without implicit arguments are capable of doing conversions. Most of the time, these conversions
//  are dangerous and hard to pin down
//  - The discrepancy between structure and intention: for example, an implicit def is never used with the meaning of
//  a “method”.
//  - The need to name implicits when we often don’t need them

// Implicit Conversions
//  - Implicit conversions now need to be made explicit. This solves a big burden.
//  - Prior to Scala 3, implicit conversions were required for extension methods and for the type class pattern. Now,
//  with Scala 3, the extension method concept is standalone, and so we can implement many of the patterns that
//  required implicits without relying on conversions.
//  - With Scala 3, conversions need to be declared in a specific way.
// Prior to Scala 3, implicit conversions were incredibly easy to write compared to their power (and danger)
case class Person(name: String) {
  def greet: String = s"Hey, $name"
}

@main def testConv(): Unit = {
  // one-liner implicit conversion
  implicit def stringToPerson(str: String): Person = Person(str)

  println("Alice".greet)
}

// with Scala 3, there are many steps to follow to make sure we know what we’re doing. An implicit conversion is a
// given instance of Conversion[A, B]
// In this way, you need to be really motivated to use implicit conversions. Coupled with the lack of proper reasons
// to use implicit conversions — we don’t need them for extension methods anymore — should make the use of implicit
// conversions drop dramatically.
given /*stringToPerson:*/ Conversion[String, Person] with {
  def apply(s: String): Person = Person(s)
}

// We still wouldn’t be able to rely on the implicit magic. We also need to specifically import the
// implicitConversions package
@main def testConv2(): Unit = {
  println("Alice".greet)
}

// Scala 3 Givens, Implicits and Naming
// you can simply a given instance without naming i
//given Ordering[String] with {
// ???
//}
// Write using clauses without naming the value which will be injected
def sortThings[A](things: List[A])(using Ordering[A]) = ???
// givens solve a syntax ambiguity when invoking methods which have using clauses
def getMapImp(implicit size: Int): Map[String, Int] = ???
@main def testImp(): Unit = {
  // even if we had an implicit in scope, because the argument will override the implicit value the compiler would
  // have inserted, and so we’ll get a type error from the compiler
  // getMapImp("Meow")
  getMapImp(12)("Meow")
}
// given solve that
def getMapGiven(using size: Int): Map[String, Int] = ???
@main def testGiven(): Unit = {
  // using is more specific and clear
  getMapGiven(using 42)("Alice")

  given x: Int = 12
  // If we do have a given Int in scope, then we can simply call getMap("Alice"), because the given value was already
  // injected into size.
  getMapGiven("Alice")
}


// How Scala 3 Givens Solve the Track-Down Problem
//  - given instances need to be explicitly imported
//  - givens are only used for automatic injection of arguments via a using clause

// Scala 3 Givens and Auto-Imports
//  - givens are automatically injected wherever a using clause for that type is present, this mechanism is similar to
//  implicits
// However, the current implicit resolution mechanism leaves very generic errors. At most, “no implicits found”. The
// Scala 3 compiler has come a long way to surface more meaningful errors, so that if the search for givens fails, it
// will show the point where the compiler got stuck, so we can provide the remaining given instances in scope.
//  new given/using mechanism works in the same way as the old implicit val/object + implicit argument, and we can
//  interoperate between them without any problems. This capability was created for our peace of mind as we move to
//  Scala 3.
// The new given/using combos in Scala 3 were created to reduce some of the power of the implicit keyword, which may
// be easily misused. The main arguments for implicits include:
//  - implicit arguments, which are now solved with given/using
//  - type classes, which are also solved with given/using
//  - extension methods, which now have their own language-level constructs in Scala
//  - implicit conversions, which now need to be explicitly enforced
// How to deal with implicit?
//  - implicit keyword has not disappeared from Scala 3. It will be slowly deprecated and eventually removed from the
//   language.
//  are we supposed to continue using implicits? How are we going to work with existing codebases?
//  - No, you shouldn’t use implicits anymore. Use the new given/using combos for implicit values/objects + implicit
//    arguments.
//  - Use givens.
// The given mechanism works in the same way as the implicit mechanism, for the purpose of finding an instance to
// insert into a method which requires it. Namely, if you specify a using clause, the compiler will look for a given
// instance defined in the following places, in order:
//  - the local scope where the method is being defined
//  - the scope of all the explicitly imported classes, objects and packages
//  - the scope of the companion object of the class whose method you’re invoking
//  - the scope of the companion objects of all the types involved in the method call, if the method is generic
// built-in summon[T] method in Scala 3.
def methodWithGivenArg[A](using instance: A) = instance
// If you call methodWithGivenArg[Int], the compiler will look for a given value of type Int in the following places:
//  - the scope where we defined the method
//  - the scope of all imports
//  - the scope of the companion object of the class where this method is defined (if we defined it in a class)
//  - the scope of all companions of the types involved in the call: in this case, the Int object
@main def testGiven1(): Unit = {
  given meow: Int = 43

  val a = methodWithGivenArg[Int]
  println(a)
}
// The exact same mechanism works in the case of a method taking an implicit argument:
//  built-in implicitly[T] method
def methodWithImplicitArg[A](implicit instance: A): A = instance
// If you call aMethodWithImplicitArg[Int], the compiler will run the exact same search for an implicit Int:
//  - scope of class/object of the method
//  - scope of imports
//  - scope of companion
//  - scope of ALL companions of the types involved, in this case the Int object
@main def testImplicitM(): Unit = {
  implicit val meow: Int = 32
  val a = methodWithImplicitArg[Int]
  println(a)
}
// In order to be able to run a smooth transition between Scala 2 and Scala 3, the mechanism for finding
// given/implicit instances is identical, so you can keep assuming a similar mental model, with a slightly different
// syntax: given/using instead of implicit
// Now, in order to be able to interface with code written with implicits, you can simply define your new methods
// with given/using, and your existing implicit values will work fine
@main def testGivenAndImplicit(): Unit = {
  implicit val theOnlyInt: Int = 32
  val a = methodWithGivenArg[Int]
  assert(a == 32)
}
// if you’re working with an old method with implicits, and you’re now defining given values, that’ll work too
@main def testImplicitGiven(): Unit = {
  given meow: Int = 32

  assert(methodWithImplicitArg[Int] == 32)
}
// At the same time, compiler will trigger ambiguities if it finds both an implicit val or a given in the same scope:
@main def testImplicitGivenCompilerError(): Unit = {
  // ambiguous implicit arguments: both value meow2 and given instance meow1 match type
  //  given meow1: Int = 32
  implicit val meow2: Int = 32
  methodWithImplicitArg[Int]
}