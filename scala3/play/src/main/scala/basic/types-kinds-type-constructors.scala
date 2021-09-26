// One of the main strengths of Scala is the increased productivity of devs by leveraging the type system, 
// Thinking about types correctly will directly influence how we design our code.

// Level 0: Value Types aka. Normal types
// Types in Scala can be organized into kinds.
// Normal types that we can attach to values, e.g. Int, String
// We call them level-0 types. This is the simplest kind of types imaginable
@main def level0(): Unit = {
  val num = 32
  val str = "hello"
}

// Level 1: “Generics”
// Level-1 is the kind of types which receive type arguments of the inferior level (level-0).
// We attach type arguments to the new type we deeclare:
// “generic”. They can work on Ints, Strings, Persons and other level-0 types
// These new types cannot be attached to a value on their own. They need to have a real type argument before we can use a value with them
class Optional[A]
// LinkedList is a higher-level type. I’ll call it a level-1 type, because it takes type arguments of the inferior kind
class LinkedList[A]
@main def level1(): Unit = {
  // We need to use a level-0 type as a type argument before we can use these new types0w0
  val o = new Optional[Int]
  val l = new LinkedList[Int]
}

// 4. Type Constructors
// We attached the type LinkedList[Int] to the previous value. We used the level-1 type LinkedList and we used the level-0 type argument Int to create a new level-0 type
// Similar to a function: take a function, pass a value to it, obtain another value
// Take a level-1 type, pass a level-0 type argument to it, obtain another level-0 type
// These generic types are also called type constructors, because they can create level-0 types. LinkedList itself is a type constructor: takes a value type (e.g. Int) and returns a value type (e.g. LinkedList[Int])
@main def type_constructor(): Unit = { }

// Level 2 and Beyond: Higher-Kinded Types
// The Scala type system moves a step further, by allowing the definitions of generic types whose type arguments are also generic. We call these higher-kinded types
class Functor[F[_]]
// The underscore marks the fact that the type argument F is itself generic (level-1). Because this new type takes a level-1 type argument, the Functor example above is a level-2 type. In order to use this type and attach it to a value, we need to use a real level-1 type
// Much like LinkedList, Functor itself is a type constructor. It can create a value type by passing a level-1 type to it

// Scala is permissive enough to allow even higher-kinded types (in my terminology, level-3 and above) with nested [_] structures:
class Meta[F[_[_]]]
@main def level2_and_beyond(): Unit = {
  val functorList = new Functor[List]
  val functorOption = new Functor[Option]
  val meta = new Meta[Functor]
}

// More Type Constructors
class HashMap[K, V]
class ComposedFunctor[F[_], G[_]]
class Formatter[F[_], A]
@main def more(): Unit = {
  val formatter = new Formatter[List, String]
}
