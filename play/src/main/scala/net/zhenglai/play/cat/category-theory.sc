// Category theory is a branch of mathematics that grew up around algebraic topology in the mid-20th century, but has
// been increasingly applied to areas of computer science, particularly type theory.
//
// We can think of a category as a generalized monoid.
//
// Observe that with a monoid M, we can view each element of the monoid as a function of type M => M.
// For example, in the Int monoid with addition, these elements are (_ + 0), (_ + 1), etc.
// Then the composition of these functions is the operation of the monoid.

// We can generalize this notion to consider not just the type M, but all types (A, B, C, etc.) and functions not
// just of type M => M, but A => B for any types A and B.
//
// Then ordinary function composition is an associative operation, with an identity element which is the identity
// function that just returns its argument.

// Monads are one of the most common abstractions in Scala, and one that most Scala programmers are familiar with
// even if they don’t know the name.
// We even have special syntax in Scala to support monads: for comprehensions.

// Despite the ubiquity of the concept, Scala lacks a concrete type to encompass monads.
// This is one of the many benefits of using Cats.

// Formally, a monad for a type M[A] has:
//  - an operation flatMap with type (M[A], A => M[B]) => M[B]
//  - an operation pure with type A => M[A].
// pure and flatMap must obey three laws:
//  - Left identity: flatMap pure(a) f == f(a)
//  - Right identity: flatMap m pure == m
//  - Associativity: flatMap (flatMap m f) g == flatMap m (a => flatMap f(a) g)

//
// * pure (often referred to as unit or return) abstracts over the constructor of our monad.
// * flatMap (often referred to as bind and given the infix notation >>=) chains structured computations together in a
// way that allows interference between the structure and computation layers.

// Most of the type classes we consider in this class are monadic: List, Vector, Option, Xor, State, Future, Parser,
// Reader, Writer, IO etc.
// That means that these type classes all have the above operations, the so-called 'proper morphisms' of a monad,
// They also include some 'non-proper morphisms' which give them additional capabilities.

// A monad is also a functor.
// Given implementations of flatMap and pure, write an implementation of map:

// There are three ways to define a monad in terms of combinations of primitive combinators. The one we've seen so
// far uses unit and flatMap.
//A second combination is unit, map, and join, where join has the following type signature:
//  def join[A](mma: F[F[A]]): F[A] = ???
//Implement join in terms of unit, map, and/or flatMap.
