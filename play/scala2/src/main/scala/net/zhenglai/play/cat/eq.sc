import cats.Eq
import cats.syntax.eq._

case class Point(x: Double, y: Double)

object Point {
  // We define our instance of Eq[Point] in the companion object, and bring the Eq instance for Double into scope for
  // the implementation:
  // Because the instance is explicitly named in Scala, you can inject another instance into the scope and it will be
  // picked up for use for the implicit.
  // In Scala's case implicits allow the compiler to do type-directed resolution of an argument locally in the scope
  // of the method call that triggered it.
  implicit val pointEqual: Eq[Point] = (x: Point, y: Point) => {
    import cats.instances.double._
    x.x === y.x && x.y === y.y
  }
}

val p1 = Point(1.0, 0.0)
val p2 = Point(1.0, 0.0)
val p3 = Point(0.0, 1.0)
(p1 === p2) // true
(p2 === p3) // false

// So the type class pattern is truly ad-hoc in the sense that:
//  1. we can provide separate implementations for different types
//  2. we can provide implementations for types without write access to their source code
//  3. the implementations can be enabled or disabled in different scopes