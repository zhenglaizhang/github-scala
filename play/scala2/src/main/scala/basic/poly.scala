package net.zhenglai
package basic

object poly {

  // Parametric Polymorphism
  // Parametric polymorphism is a pattern in which polymorphic functions are written without mention of any specific type,
  // and can thus apply a single abstract implementation to any number of types in a transparent way.
  //
  // Monomorphic methods can only be applied to arguments of the fixed types specified in their signatures (and their
  // subtypes, we’ll see in a moment).
  //
  // Polymorphic methods can be applied to arguments of any types which correspond to acceptable choices for their
  // type parameters.

  def slength(s: String): Int = s.length

  def length[A](xs: List[A]): Int = xs.length

  // We can mix in parametric polymorphism at the trait level, as well as add upper and lower type bounds:
  trait Plus[A] {
    def plus(x: A): A
  }

  def plus[A <: Plus[A]](a1: A, a2: A): A = a1.plus(a2)

  // Subtype Polymorphism
  // The methods that we’ve been calling monomorphic are only monomorphic in the sense of parametric polymorphism,
  // and they can in fact be polymorphic in the traditional object-oriented way.

  trait Base {
    def foo: Int
  }

  class Derived1 extends Base {
    override def foo = 1
  }

  class Derived2 extends Base {
    override def foo = 2
  }

  // no type parameters
  // parametrically monomorphic.
  // it can be applied to values of more than one type as long as those types stand in a subtype relationship to the fixed Base type which is specified in the method signature
  def subtypePolymorphic(b: Base): Int = b.foo
  subtypePolymorphic(new Derived1) // 1
  subtypePolymorphic(new Derived2) // 2

  // In Scala, parametric and subtype polymorphism are unified by the concept of variance.
  // Variance refers to a categorical framework undergirding the type system
  // A type can be covariant when it does not call methods on the type that it is generic over. If the type needs to
  // call methods on generic objects that are passed into it , it cannot be covariant.
  //
  // A type can be contravariant when it does call methods on the type that it is generic over. If the type needs to
  // return values of the type it is generic over, it cannot be contravariant.

  // Strongly typed functional languages like Scala and Haskell have function types.
  trait Function1[-T1, +R] {
    def apply(v1: T1): R
  }

  // In particular, Cat <: Animal implies:
  //  - Function1[Animal, Double] <: Function1[Cat, Double]
  //  - Function1[Double, Cat] <: Function1[Double, Animal]

  // In general it is safe to substitute a function f for a function g
  // if f accepts a more general type of arguments and returns a more specific type than g.
  // For example, a function of type Cat=>Cat can safely be used wherever a Cat=>Animal was expected, and likewise a
  // function of type Animal=>Animal can be used wherever a Cat=>Animal was expected.
  //
  // One can compare this to the robustness principle of communication:
  //  "be liberal in what you accept and conservative in what you produce".
  // Rest assured that the Scala compiler will complain if you violate these rules:

  // Ad-hoc Polymorphism
  // Ad-hoc polymorphism refers to when a value is able to adopt any one of several types because it, or a value it
  // uses, has been given a separate definition for each of those types.

  // The term ad hoc in this context is not intended to be pejorative; it refers to the fact that this type of
  // polymorphism is not a fundamental feature of the type sytem.
  //  For example, the + operator essentially does something entirely different when applied to floating-point values
  //  as compared to when applied to integers.
  //
  // Ad-hoc polymorphism in Scala (with a large debt to Haskell) is most closely associated with what is known as the
  // type class pattern.

  // Type classes define a set of contracts that the client type needs to implement.
  // Type classes are commonly misinterpreted as being synonymous with interfaces in Java or other programming
  // languages.
  //
  // The focus with interfaces is on subtype polymorphism.
  //
  // However with type classes the focus is on parametric polymorphism: you implement the contracts that the type
  // class publishes across unrelated types.
  //
  // A second crucial distinction between type classes and interfaces is that for class to be a "member" of an
  // interface it must declare so at the site of its own definition.

  // By contrast, any type can be added to a type class at any time, provided you can provide the required
  // definitions, and so the members of a type class at any given time are dependent on the current scope.
  //
  // Type classes are similar to the GoF adapter pattern, but are generally cleaner and more extensible.

  // There are three important components to the type class pattern: the type class itself, instances for particular
  // types, and the interface methods that we expose to users.
  //
  // The instances of a type class provide implementations for the types we care about, including standard Scala types
  // and types from our domain model.

  // The type class itself is a generic type that represents the functionality we want to implement:
  trait Show[A] {
    def format(value: A): String
  }

  // We define instances by creating concrete implementations of the type class and tagging them with the implicit
  // keyword:
  object ShowInstances {
    implicit val stringPrintable = new Show[String] {
      override def format(value: String): String = "show: " ++ value
    }
    implicit val intPrintable = new Show[Int] {
      override def format(value: Int): String = "show: " ++ value.toString
    }
  }

  // Instances are then exposed to users through interface methods.
  // Interfaces to type classes are generic methods that accept instances of the type class as implicit parameters.
  // There are two common ways of specifying an interface:
  //  - Interface Objects
  //  - Interface Syntax.

  // Interface Objects
  // The simplest way of creating an interface is to place the interface methods in a singleton object.
  object Printer {
    def format[A](a: A)(implicit s: Show[A]): String = s.format(a)
    def print[A](a: A)(implicit s: Show[A]): Unit = println(format(a))
  }
  import ShowInstances._
  Printer.print(12)

  // Interface Syntax
  // Interface Syntax
  //  Another approach is to use type enrichment to extend existing types with interface methods.
  //  Typelevel libraries often refer to this as “syntax” for the type class:
  object ShowSyntax {
    implicit class ShowOps[A](a: A) {
      def format(implicit s: Show[A]): String = s.format(a)
      def print(implicit s: Show[A]): Unit = println(s.format(a))
    }
  }
  import ShowSyntax._
  "foo".print
  23.print

}
