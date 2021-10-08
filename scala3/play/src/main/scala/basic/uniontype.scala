package net.zhenglai
package basic
import java.io.File

// Union Types
// - a combined type (“either A or B”) in the same declaration. 
// - However, this has nothing to do with the Either monadic type
def process(i: Int): String | Int | Double =
  if (i < 0) then "Negative"
  else if (i < 10) then i.toDouble
  else i

type ErrorOr[A] = A | "error"
// Everything happens at compile time
def handleResource(file: ErrorOr[File]): Unit = file match {
  case _: "error" => println("this is error")
  case f: File => println("a good file")
}

@main def ut() = {
  // The compiler will still compute the lowest common ancestor type by way of inheritance. 
  // That said, we can define a union type explicitly if we wanted
  val strOrInt = if (43 > 0) "str" else 43 // Any, inferred by compiler
  val strOrInt1: String | Int = if (32 > 0) "str" else 43

  val s: String | Double | Int = process(12)
  // The caveat of having a union type in a method/function is that in order to use the types properly, e.g. use their methods, 
  // we have to pattern match the union-typed value against the possible type
  s match {
    case s: String => println(s)
    case i: Int => println(i)
    case d: Double => println("null")
  }
}