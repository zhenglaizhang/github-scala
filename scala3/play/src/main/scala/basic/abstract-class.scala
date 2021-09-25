// Scala has a single-class inheritance model, where one can extend a single class.
// Scala also has the concept of an abstract class, where one can
//  - restrict the ability of a class to be instantiated
//  - add unimplemented fields or methods
// An abstract class may or may not have either abstract (unimplemented) or non-abstract (implemented) methods.

// However, traits also share most of the same capabilities:
//  - they can’t be instantiated by themselves
//  - they may have abstract (unimplemented) fields or methods
//  - (more importantly) they may have non-abstract (implemented) fields and methods as well

abstract class Person1 {
  val canDrive: Boolean
  def discussWith(another: Person1): String
}

// There is little practical difference between an abstract class and a trait if you extend a single one
// Multiple traits may be used in the inheritance definition of a class, whereas a single abstract class can be extended
// The famous diamond problem. Scala solves it with a dedicated mechanism called linearization
// An abstract class can take constructor arguments
// Scala 3 will also allow constructor arguments for traits

// For good practice, it is always a good idea to make code easy to read, understand and reason about.
// To this aim, we generally represent “things” as classes, and “behaviors” as traits.
// This conceptual difference is directly mapped in the language: a “thing” (class) can be a specialized version of another “thing” (also a class), but might incorporate multiple “behaviors” (traits).
