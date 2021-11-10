package fp

trait Functor[F[_]] {
  // def map[A, B](fa: F[A])(f: A => B): F[B]
}

//extension[F[_], A, B] (fa: F[A])(using functor: Functor[F])
  // def map(f: A => B): F[B] = functor.map(fa)(f)