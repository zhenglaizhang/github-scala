package net.zhenglai
package basic

import scala.Nothing
import scala.NotImplementedError

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
