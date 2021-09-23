package net.zhenglai
package basic

// what's variance
// if dogs are animals, could lists of dogs be considered lists of animals as well? This is the variance question, i.e. whether the subtype relationship can be transferred to generic types.
object controvariance {
  // yes, then we consider the generic type covariant
  abstract class List[+A]

  // We can also have no variance (no + sign), which makes a generic type invariant. This is the Java way of dealing
  // with generics

  // “hell no” or “no, quite the opposite”. This is contravariance.
}

trait Vet[-A] {
  def heal(animal: A): Boolean
}
// if you ask me “Daniel, gimme a vet for my dog”
// and I’ll give you a vet which can heal ANY animal,
// not just your dog, your dog will live.

@main def contr() = {
  class Animal
  class Dog(name: String) extends Animal
  val md = new Dog("wow")
  val myVet: Vet[Dog] = new Vet[Animal] {
    override def heal(animal : Animal) : Boolean = true
  }
  myVet.heal(md)
}
// Here’s a rule of thumb: when your generic type “contains” or “produces” elements of type T, it should be covariant
// . When your generic type “acts on” or “consumes” elements of type T, it should be contravariant.
//
//Examples of covariant concepts: a cage (holds animals), a garage (holds cars), a factory (creates objects), a list
// (and any other collection). Examples of contravariant concepts: a vet (heals animals), a mechanic (fixes cars), a
// garbage pit (consumes objects), a function (it acts on/it’s applied on arguments).