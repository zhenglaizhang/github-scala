package net.zhenglai
package play.cat

//
//   Semigroup
//      |
//      |
//    Monoid
//
//
//
//  Functor                 Semigroupal
//    |                         |
//    |                         |
//  ------------------     ------
// |                 |     |
// |                  Apply
// FlatMap              |
// |                Applicative
// |                  | |
// |------------------
//        |             |
//      Monad     ApplicativeError
//        |               |
//         --------------
//            |
//        MonadError
//
//
object MonoidTestDef {
  // A Semigroup is a type class granting the capability of a type to combine two values of that type and produce
  // another value of that same type
  // We can use Semigroups whenever we write generic code and operate with values that need to be combined:
  //  - numbers
  //  - strings
  //  - shopping carts in an online store
  //  - permissions in a data repository
  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }

  // Monoids extend Semigroups
  // Monoids are a special kind of semigroups, where besides the combination function, we also have a “neutral
  // element” of that combination function. We call that value empty, or “zero” (with the proper quotes because zero
  // has a special meaning in math, you know).
  // The property is that
  //  - combine(x, empty) == x
  //  - combine(empty, x) == x
  //  for all elements x of type A
  trait Monoid[A] extends Semigroup[A] {
    def empty: A
  }

  object Monoid {
    def apply[A](implicit m: Monoid[A]): Monoid[A] = m
  }
}

object MonadDef {
  //  Cats has a rule of thumb when it deconstructs type classes: in general, each type class has one fundamental method
  // functors describe the capability to “map” containers, such as lists, options, sets, futures,
  // It's high kinded,
  trait Functor[F[_]] {
    // transforming an instance to another type of instance through a function, i.e. a map
    def map[A, B](a: F[A])(f: A => B): F[B]
  }

  // Monads establish most of the equivalence between imperative programming and functional programming. An
  // imperative program can easily be transformed into FP by creating a monadic type capable of chaining each
  // “instruction” as a new (pure) value. “Do this, do this, and then this” becomes “new monad, flatMap to new monad,
  // then flatMap to a final monad”.
  // In order to keep its promise and bridge the concept of “imperative” to FP, the Monad trait has another
  // fundamental method that can “iterate”. That method is called tailrecM, which brings stack safety to an
  // arbitrarily large sequence of flatMaps. The flatMap method belongs to a different type class, which bears the
  // (perhaps uninspired) name of FlatMap
  trait FlatMap[F[_]] extends Functor [F] {
    // chaining the computation of instances based on dependent plain values, i.e. a flatMap
    def flatMap[A, B](a: F[A])(f: A => F[B]): F[B]
  }

  // The main intuition of monads is the “chained” computations of FP. Therefore, the pure method should be the one
  // to go into a separate type class.
  // That type class is called Applicative, and it sits between Functor and Monad
  // Applicative is the type class with the capability to wrap a plain value into a wrapped type
  trait Applicative[F[_]] extends Apply[F] {
    // creating an instance of this magical data type (whatever the type is) out of a plain value
    def pure[A](a: A): F[A]
  }

  // Monads are the sweet spot of pure FP.
  // They encapsulate chainable computations
  // Monads have two capabilities:
  //  - the capability to “lift” a plain value into a monadic type, an operation known as pure
  //  - the capability to “chain” computations of monadic types, an operation known as flatMap or bind
  trait Monad[F[_]] extends FlatMap[F] with Applicative[F] {
    // for free
    // Monad should extend Functor
    def map[A, B](fa: F[A])(f: A => B): F[B] =
      flatMap(fa)(a => pure(f(a)))
  }

  // Think of two lists. Whenever we write a for-comprehension of two lists (or a flatMap), we’re doing a cartesian
  // product of those two lists.
  // The concept of a cartesian product (which is not the same as a flatMap) is core to the type class called
  // Semigroupal.
  trait Semigroupal[F[_]] {
    // Semigroupal has a method that takes two wrapped values and returns a wrapped value of tuple(s). This is the
    // (very general) concept of a cartesian product over any type F
    def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]
  }

  // Apply is a weaker (but more general) applicative, and it sits between Applicative and Functor
  // It’s a higher-kinded type class (much like Applicative, Functor and Monad) which allows us to invoke a wrapped
  // function over a wrapped value and obtain a wrapped result
  trait Apply[F[_]] extends Functor[F] with Semigroupal[F] {
    def ap[A, B](f: F[A => B], fa: F[A]): F[B]

    // With the ap method and the map method from Functor, we can implement the following method for free
    // This means if Apply extends Functor, then it naturally extends Semigroupal as well.
    override def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] = {
      val f: A => (B => (A, B)) = (a: A) => (b: B) => (a, b)
      val fab: F[B => (A, B)] = map(fa)(f)
      ap(fab, fb)
    }

    // real life method
    // The mapN method not only does a (cartesian) product between two wrapped values, but it also applies a function
    // to the elements being tupled
    def mapN[A, B, C](fa: F[A], fb: F[B])(f: (A, B) => C): F[C] = {
      map(product(fa, fb)) {
        case (a, b) => f(a, b)
      }
    }
  }

  // Applicatives which can wrap successful values of type A into a wrapped type F[A], we can also wrap error types
  // and treat them in the same way
  trait ApplicativeError[F[_], E] extends Applicative[F] {
    // The raiseError method can take an undesirable, “error” value and wrap that into a wrapped type F[A]. Notice
    // that the error type E does not appear in the result type F[A] — that’s because we treat wrapped types in the
    // same way down the line, regardless of whether they’re successful or not, and treat the error cases later in a
    // purely functional way if we need to.
    def raiseError[A](error: E): F[A]
  }

  // We have an error-enhanced monadic type as well, called MonadError
  trait MonadError[F[_], E] extends ApplicativeError[F, E] with Monad[F] {
    def raiseError[A](e: E): F[A]

    def handleErrorWith[A](fa: F[A])(f: E => F[A]): F[A]

    def handleError[A](fa: F[A])(f: E => A): F[A]

    def ensure[A](fa: F[A])(e: E)(f: A => Boolean): F[A]
  }
}

// todo data validation, purely functional state, modes of evaluation, traversing, Kleisli, type class variance