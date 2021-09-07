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


case class <+>[A, B](a: A, b: B)
val x: Int <+> Double = <+>(1, 2.0)
//1 <+> 2.0
// value <+> is not a member of Int
extension[A, B](a: A)
  def <+>(b: B): <+>[A, B] = new <+>(a, b)

1 <+> 2.1
<+>(1)(2.1)


object Foo:
  def one: Int = 1

extension (foo: Foo.type)
  def add(i: Int): Int = i + foo.one

Foo.add(12)
Foo.add(1)
