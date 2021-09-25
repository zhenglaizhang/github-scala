import scala.concurrent.duration.*

// Implicit classes
// Enriching Types
// Implicit classes are one-argument wrappers, i.e. a class with one constructor argument,
// with regular methods, fields, etc, except that they have the implicit keyword in their declaration
// Usually, libraries (including the standard library) packs implicits into “Ops”-like objects
object MyStringOps {
  implicit class MyRichString(str: String) {
    def fullStop: String = str + "."
  }

}
object durationtest {
  val a = 20.seconds
  val b = 20 seconds

  val range = 1 to 10

  import MyStringOps.*

  // extension methods
  // making them seem like they’re part of the language or standard library
  new MyRichString("this is a sentence").fullStop
  "this is a sentence".fullStop
}
