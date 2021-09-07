// extension method

// scala2
implicit final class MyArrowAssoc[A](private val self: A) {
  def -->[B](y: B): (A, B) = (self, y)
}

12 --> 3
12 -> 3

// scala3
// no implicit convension
extension[A](a: A)
  def ~>[B](b: B): (A, B) = (a, b)

extension[A, B](a: A)
  def ~~>(b: B): (A, B) = (a, b)

12 ~> 3
12 ~~> 3
~>(12)(3)