// Nesting Types
// Classes, objects and traits can hold other classes, objects and traits, as well as define type members — abstract or concrete in the form of type aliases
// The question of using those types from inside the Outer class is easy: all you have to do is just use those nested classes, objects or type aliases by their name.
// The types of the style instance.MyType and Outer#Inner are called path-dependent types, because they depend on either an instance or an outer type (a “path”)
class Outer {
  class Inner
  object InnerObj
  type InnerType
  def process(inner: Inner): Unit = ???

  // There is a parent type for all Inner types: Outer#Inner
  // The type Outer#Inner is called a type projection. With this definition, we can now use Inner instances created by any Outer instance
  def processGeneral(inner: Outer#Inner): Unit = ???
}

class AbstractRow {
  type Key
}

object AbstractRow {
  // Rely on the type of the argument to return the appropriate nested type
  // Besides generics, this is the only technique I know that would allow a method to return a different type depending on the value of the argument(s)
  def getIdentier(row: AbstractRow): row.Key = ???
}

// The question of using those types from outside the Outer class is a bit trickier.
@main def pdt(): Unit = {
  val outerA = new Outer
  val outerB = new Outer
  val innerA: outerA.Inner = new outerA.Inner
  val innerB: outerB.Inner = new outerB.Inner
  assert(outerA.InnerObj != outerB.InnerObj)
  // Similarly, the abstract type member InnerType is different for every instance of Outer

  // With this kind of method, we can only pass Inner instances that correspond to the Outer instance that created them
  // This is expected, since innerA and innerB have different types. However, there is a parent type for all Inner types: Outer#Inner
  outerA.process(innerA)
  // outerA.process(innerB) // compilation failed
  outerB.process(innerB)

  outerA.processGeneral(innerA)
  outerB.processGeneral(innerA)
}

// Motivation for Path-Dependent Types and Type Projections
// - a number of libraries use type projections for type-checking and type inference. Akka Streams, for example, uses path-dependent types to automatically determine the appropriate stream type when you plug components together: for example, you might see things like Flow[Int, Int, NotUsed]#Repr in the type inferrer
// - type lambdas used to rely exclusively on type projections in Scala 2, and they looked pretty hideous (e.g. { type T[A] = List[A] }#T ) because it was essentially the only way to do it. Thank heavens we now have a proper syntactic construct in Scala 3 for type lambdas
// - you might even go bananas and write a full-blown type-level sorter by abusing abstract types and instance-dependent types along with implicits (or givens in Scala 3).

// Functions with Dependent Types
// This is new in Scala 3. Prior to Scala 3, it wasn’t possible for us to turn methods like getIdentifier into function values so that we can use them in higher-order functions (e.g. pass them as arguments, return them as results etc). Now we can, by the introduction of the dependent function types in Scala 3. Now we can assign the method to a function value
@main def dtf(): Unit = {
  import AbstractRow.*
  val getIdentierFunc: (r: AbstractRow) => r.Key = getIdentier
  val getIdentierFuncFull: (r: AbstractRow) => r.Key = new Function1[AbstractRow, AbstractRow#Key] {
    def apply(arg: AbstractRow): arg.Key = ???
  }
  // which is a subtype of Function1[AbstractRow, AbstractRow#Key] because the apply method returns the type arg.Key, which we now know that it’s a subtype of AbstractRow#Key, so the override is valid.
}

// Let’s recap:
//  - we covered nested types and the need to create different types for different outer class instances
//  - we explored type projections, the mother of instance-dependent types (Outer#Inner)
//  - we went through some examples why path-dependent types and type projections are useful
//  - we discussed dependent methods and dependent functions, the latter of which is exclusive to Scala 3
