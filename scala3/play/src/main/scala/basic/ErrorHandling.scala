// 1. try/catches are almost always undesirable,
// 2. Try wraps failed computations into values we can then process and handle as we see fit,
// 3. Either expands on the concept by considering errors to be valuable information of any type, and
// 4. Validated adds extra power by the capacity to combine errors and values.

// Idiomatic Error Handling in Scala
// Throwing and Catching Exceptions
@main def demoTry(): Unit = {
  // try/catch structure is an expression
  val magicChar = try {
    val scala = "Scala"
    scala.charAt(20)
  } catch {
    case e: NullPointerException => 'z'
    case r: RuntimeException => 'z'
  } finally {
    // finally part doing things outside of value computations might cause a small aneurysm
    println("cleaning up")
  }
}
// Try is Better
// Try has the map, flatMap and filter methods, much like regular collections. We can think of Try as a “collection”
// with maybe one value
@main def demoTry2(): Unit = {
  import scala.util.Try
  val g = Try(22)
  val m = g.map(_ + 1)
  val bad = (x: Int) => Try(x / 0)
  val bad2 = g.flatMap(bad)
  val g2 = bad2.recover {
    case _: ArithmeticException => -1
  }
  val chain = for {
    x <- g
    y <- g2
  } yield x + y
  println(chain)
}

object mytry {
  sealed abstract class Try[+A]

  case class Success[+A](value: A) extends Try[A]

  case class Failure(reason: Throwable) extends Try[Nothing]

  object Try {
    def apply[A](computation: => A): Try[A] =
      try {
        Success(computation)
      } catch {
        case e: Throwable => Failure(e)
      }
  }
}

@main def testMyTry(): Unit = {
  import mytry.*
  val t = Try {
    val scala = "Scala"
    scala.charAt(20)
  }
  println(t)
}

// Either This or That
// Try is based on the assumption that an exception is also a valid data structure. Fundamentally, an error in an
// application is also valuable information. Therefore, we should treat it as such, by manipulating this information
// into something useful. In this way, we reduce the notion of “error” to a value.
// So an “error” can be whatever is valuable for you to handle.
object myeither {
  sealed abstract class Either[+A, +B]

  case class Left[+A, +B](value: A) extends Either[A, B]

  case class Right[+A, +B](value: B) extends Either[A, B]
}

// Either has the same benefits as Try
// Either has the liberating benefit of creating and handling any type you’d like as an “error”.
@main def testeither(): Unit = {
  import scala.util.Either
  type MyError = String
  val success: Either[MyError, Int] = Right(22)
  val modifiedC = success.map(_ + 1)
  val bad = (x: Int) => if (x == 0) Left("Cannot divide by 0") else Right(45 / x)
  val failed = Right(0).flatMap(bad)
  val recovered = failed.orElse(Right(-1))
  val chain = for {
    x <- success
    y <- recovered
  } yield x + y
  println(chain)
}

// Validated
// Besides doing pretty much everything that Either does, Validated allows us to accumulate errors. One obvious use
// case is online forms that have to meet certain criteria. I