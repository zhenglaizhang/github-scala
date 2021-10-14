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
@main def testGiven(): Unit = {
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