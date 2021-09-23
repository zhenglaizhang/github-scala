package net.zhenglai
package basic

// 1) What the weird structure means
// 2) What do you do when you want to enforce that a trait MUST be mixed into a type you’re defining.
object selftype {}

// Enforcing type constraints

trait Edible

trait Person {
  def hasAllergiesFrom(thing: Edible): Boolean
}
trait Child extends Person
trait Adult extends Person

trait Diet {
  def eat(thing: Edible): Boolean
}

//trait Carnivore extends Diet
//trait Vegetarian extends Diet

// The problem is that you want the diet to be applicable to Persons only.
// Not only that, but your functionality actually relies on logic from the Person class.
object opt1Inheritance {
  // This option makes all the (non-private) Person functionality available to Diet. However, this obviously makes a
  // mess out of two otherwise clean and separate concept hierachies.
  trait Diet extends Person
}

object opt2Generics {
  trait Diet[P <: Person]
  // This option adds a degree of separation by adding a type argument.
  // You have access to the Person type, but not to its methods - you would need an instance of Person to access the logic, but then you’d need to pass a Person as a constructor argument.
}

object opt3SelfType {
  // This is called a self-type. It looks like a lambda structure, but it has a completely different meaning.
  // whichever class implements the Diet trait MUST ALSO implement the Person trait
  trait Diet { self: Person =>
    def eat(thing: Edible): Boolean =
      if (self.hasAllergiesFrom(thing)) false
      else true
  }
  trait Carnivore extends Diet with Person
  trait Vegetarian extends Diet with Person

  class VegAthlete extends Vegetarian with Adult {
    override def hasAllergiesFrom(thing: Edible): Boolean = true
  }

  // The main advantage of using this structure is that you can use the Person functionality directly in the Diet trait, without otherwise creating any type relationship between the two hierarchies:
}

// Punchline
// What the fundamental difference between self-types and inheritance
trait Animal
class Dog extends Animal
// Dog IS AN Animal

// Diet REQUIRES A Person.
// So remember the “is a” vs “requires a” distinction
