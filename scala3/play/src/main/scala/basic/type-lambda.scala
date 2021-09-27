// todo: understand more
// Quickly recap
//  - Scala types belong to kinds. Think of kinds as types of types.
//  - Plain types like Int, String or your own non-generic classes belong to the value-level kind — the ones you can attach to values.
//  - Generic types like List belong to what I called the level-1 kind — they take plain (level-0) types as type arguments.
//  - Scala allows us to express higher-kinded types — generic types whose type arguments are also generic. I called this kind the level-2 kind.
//  - Generic types can’t be attached to values on their own; they need the right type arguments (of inferior kinds) in place. For this reason, they’re called type constructors.

// Types Look Like Functions
// think of List (the generic type itself) as similar to a function, which takes a level-0 type and returns a level-0 type.
//  - [T] =>> Map[String, T] is a type which takes a single type argument T and “returns” a Map type with String as key and T as values
//  - [T, E] =>> Either[Option[T], E] is a type which takes two type arguments and gives you back a concrete Either type with Option[T] and E
//  - [F[_]] =>> F[Int] is a type which takes a type argument which is itself generic (like List) and gives you back that type, typed with Int (too many types, I know)
@main def typelikefunction(): Unit = {
  // type Scala2Type = ({ type T[A] = List[A] })#T

  // A type that takes a type argument X and results in the type List[X]
  // Does the exact same thing as the List type (by itself): takes a type argument and results in a new type
  type Scala3Type = [X] =>> List[X]
}

// Why We Need Type Lambdas
//  - Type lambdas become important as we start to work with higher-kinded types
object wtl {
  trait Monad[M[_]] {
    def pure[A](a: A): M[A]
    def flatMap[A, B](m: M[A])(f: A => M[B]): M[B]
  }
  // Either takes two type arguments, whereas Monad requires that its type argument take only one
  abstract class EitherMonad[T] extends Monad[[E] =>> Either[T, E]] {}
  // This EitherMonad could work for both Either[Exception, Int] and Either[String, Int] where Int is the desired type
  // Given an error type E, we’d like EitherMonad to work with Either[E, Int] whatever concrete E we might end up using
  // It’s as if we had a two-argument function, and we needed to pass a partial application of it to another function.
  // Prior to Scala 3, libraries like Cats used to resort to compiler plugins (kind-projector) to achieve something akin to the ? structure above. Now in Scala 3, it’s expressly permitted in the language
}
