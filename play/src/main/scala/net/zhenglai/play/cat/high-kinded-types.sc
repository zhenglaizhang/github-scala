// The bread and butter of everyday functional programming is the implementation of standard functional combinators for
// your datatypes.
// For example, fluency with the flatMap combinator, also known as >>=, is very important.

// e.g
//def flatMap[B](f: A => List[B]): List[B]
//def flatMap[B](f: A => Option[B]): Option[B]
//def flatMap[B](f: A => Either[E, B]): Either[E, B]
//def flatMap[B](f: A => State[S, B]): State[S, B]

// When you write functions using flatMap, in any of the varieties above,
// these functions inherit a sort of sameness from the underlying flatMap combinator.
// For example, supposing we have map and flatMap for a type, we can ‘tuple’ the values within:

def tuple[A, B](x: List[A], y: List[B]): List[(A, B)] = x.flatMap(a => y.map((a, _)))
def tuple[A, B](x: Option[A], y: Option[B]): Option[(A, B)] =
  for {
    a <- x
    b <- y
  } yield (a, b)

def tuple[E, A, B](x: Either[E, A], y: Either[E, B]): Either[E, (A, B)] =
  for {
    a <- x
    b <- y
  } yield (a, b)

// Functional Programming in Scala introduces several such functions, such as sequence and traverse.
tuple(List(1, 2), List(3.0, 4.0, 5))
tuple(Some(1), Some(3))
tuple(Some(1), None)

// In tuple’s case, what is different are the flatMap and map combinator implementations, and the type
// constructor: List, Option, State, etc.

// We would like to follow the DRY principle and parameterize: extract the parts that are different to arguments, and
// recycle the common code for all situations.

// a kind of type-level parametric polymorphism
// zero-order       42                          Int
// first-order  (x: Int) => x                   List[]
// second-order (f: (Int => Int)) => f(42)      Monad[M[]]

// The parameter declaration F[_] means that F may not be a simple type,
// like Int or String, but instead a one-argument type constructor, like List or Option.
def tupleF[F[_], A, B](x: F[A], y: F[B]): F[(A, B)] = ???

// More complicated and powerful cases are available using partial application.
//
// That’s how we can fit State, Either, Reader, Writer and other type constructors with two or more parameters..

// how to implement tupleF, we need something from F itself
// One option is to use subtype polymorphism, we could implement a trait that itself uses a type constructor
// parameter, then make every type we’d like to support inherit from it:
trait Monadic[F[_], +A] {
  def map[B](f: A => B): F[B]
  def flatMap[B](f: A => F[B]): F[B]
  def tupleF2[F[_], A, B](x: Monadic[F, A], y: Monadic[F, B]): F[(A, B)] = x.flatMap(a => y.map(a -> _))
}
// There are several major problems with the subtyping generalization in Monadic.
// These are sufficiently bad to make subtype polymorphism extremely unpopular in the Scala functional community,
// though you occasionally see it in older code
//  - First, this pattern can only be applied to classes that we have written ourselves.
//  - Second, the knowledge required to work out the new type signature above is excessively magical:

// Luckily type classes provide an elegant solution to constraining higher-kinded types.

trait Monad[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
}

// We then define instances for the types we’d like to have this on: Monad[List], Monad[Option], etc. Finally, we
// just add an implicit argument to tupleF:
def tupleF3[F[_], A, B](x: F[A], y: F[B])
                       (implicit F: Monad[F]): F[(A, B)] =
  F.flatMap(x){a => F.map(y)(a -> _)}

// Since version 2.8, Scala has provided syntax for implicit parameters, called Context Bounds.
// We can remove the implicit F argument, replacing it with a context bound F[_]: Monad in the type argument list.
def tupleF4[F[_]: Monad, A, B](x: F[A], y: F[B]): F[(A, B)]
  = implicitly[Monad[F]].flatMap(x)(a => implicitly[Monad[F]].map(y)(a -> _))
