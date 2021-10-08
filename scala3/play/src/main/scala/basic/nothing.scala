package net.zhenglai
package basic

import scala.Nothing
import scala.NotImplementedError
import scala.Option

// null, Null, None, Nil, Unit and Nothing
//  - the null reference belongs to its own distinct type, which is called Null
//  - the Null type has no methods, no fields, cannot be extended or instantiated
//  - Null extends all reference types, Null is a proper subtype of all reference types.
//  - Using null incentivizes us to write imperative, Java-style, defensive and error-prone code

//  - Nil is the empty implementation of a List
//  - Instead of null, we commonly use Options,
//    which are data structures meant to represent the presence or absence of a value

//  - the equivalent of “void” in other languages is Unit in Scala

//  - Nothing is the type of no value at all.
//  - Nothing can’t be instantiated and has no values of that type.
//  - Nothing truly means nothing: not even null, not Unit, not None, nothing at all.
//  - The only expressions that return Nothing are throwing exceptions
//  - Nothing is a proper subtype for all possible types in Scala.
//  - Much like Null, Nothing is treated in a special way by the compiler
object nulla {
  val novalue: String = null
  var theNullRef: Null = null
  val noList: List[Int] = theNullRef

  val emptyList: List[Int] = Nil
  val emptyListLength: Int = Nil.length

  val anAbsentValue: Option[String] = Option(novalue)
}

// Scala is fully object-oriented, but the type hierarchy is not so standard.
object nothing {
  // Nothing as a return type is used for expressions which never return normally, i.e. crash your JVM.
  def gimmeNoNumber(): Int = throw new NoSuchElementException // Nothing
  def gimmeNoString(): String = throw new NoSuchElementException // Nothing
  class Person
  def gimmeNoPerson(): Person = throw new NoSuchElementException // Nothing

  def funAboutNothing(x: Nothing): Int = 43

  // which compiles fine, but it will crash before your precious 56 would get returned.
  funAboutNothing(throw new RuntimeException)

  def ??? : Nothing = throw new NotImplementedError

}

// The throw expression returns Nothing,
// which is a type that does not and cannot have any instances.
// Not only that, but Nothing doesn’t have any values at all.

// Aside from crashing your program (which is a side effect),
// throw expressions return Nothing and they can be used as a replacement for any return type.

// Nothing can replace anything,
// meaning that Nothing is a valid substitute for any type.
// Nothing is a valid subtype for all types

// Use Nothing as a generic type argument
// MyNil list can hold values of type Nothing… of which there aren’t any, so it’s an empty list
trait MyList[+A]
case object MyNil extends MyList[Nothing]
