// How to create mutable data structures in Scala that feel “natively” mutable
// Either the age_=(a: int) or the update method

// Scala is primarily targeted at established software engineers. That means that — with a few exceptions — most
// Scala developers did not pick Scala as their first language
// In other words, most of us grew with mutability as a core principle of writing code.
// Code using variables and mutable data structures is harder to read and understand, and more error-prone especially
// in a multithreaded/distributed setting.
// Mutability is still useful for performance and for interacting with other code (e.g. from Java).

object mutability {
  // Mutate a variable
  var aMutable = 21
  aMutable = 1
  // Learning FP is a different style of thinking, and changing variables prevents unlearning.

  // mutate a data structure
  //  - both the getter and setter need to be present
  //  - the setter needs to have the signature def myField_=(value: MyType): Unit
  //  - the getter needs to have the signature def myField: MyType
  class Person(private var n: String, private var a: Int) {
    var nAgeAccess = 0

    // setter
    def age_=(newAge: Int): Unit = {
      println(s"Person $n changed from $a to $newAge")
      a = newAge
    }

    // getter
    def age: Int = {
      nAgeAccess += 1 // side effect, logging access times
      a
    }

    // Scala allows us to create mutable data structures with the update semantics of arrays. In other words, we can
    // define classes which are “updateable” like arrays.
    // This accessing style is easy: just add an apply method to the Person class taking an integer as argument:
    def apply(index: Int) = index match {
      case 0 => n
      case 1 => a
      case _ => throw new IndexOutOfBoundsException("Person.apply")
    }

    // Besides apply, there is another method called update which is treated in a particular way by the compiler.
    def update(index: Int, value: Any): Unit = index match {
      case 0 => n = value.asInstanceOf[String]
      case 1 => age = value.asInstanceOf[Int]
      case _ => throw new IndexOutOfBoundsException("Person.update")
    }
  }

  // Updating data in collections
  // Scala Array directly mapped to JVM native arrays

  def main(args: Array[String]): Unit = {
    val alice = new Person("Alice", 3)
    // sugar
    alice.age = 12
    // compiler rewrites to alice.age_=(12)
    println(alice.age)
    println(alice.age)
    println(alice.age)
    println(alice.nAgeAccess)

    // array access semantics
    println(alice(0))
    println(alice(1))

    // So you can mutate your data structures in the same style as arrays. The only restriction is that the update
    // method needs to take two arguments, the first one being an Int.
    alice(0) = "Jim"
    alice(1) = 100
    println(alice.age)
    println(alice(0))
  }
}