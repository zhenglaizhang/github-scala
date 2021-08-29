package net.zhenglai
package types

import org.scalatest.FunSuite

// TODO: https://stackoverflow.com/questions/34242536/linearization-order-in-scala/34243727#34243727
/**
  * An intuitive way to reason about linearisation is to refer to the construction order and to visualise the linear hierarchy.
  * You could think this way. The base class is constructed first; but before being able of constructing the base
  * class, its superclasses/traits must be constructed first (this means construction starts at the top of the hierarchy). For each class in the hierarchy, mixed-in traits are constructed left-to-right because a trait on the right is added "later" and thus has the chance to "override" the previous traits. However, similarly to classes, in order to construct a trait, its base traits must be constructed first (obvious); and, quite reasonably, if a trait has already been constructed (anywhere in the hierarchy), it is not reconstructed again. Now, the construction order is the reverse of the linearisation. Think of "base" traits/classes as higher in the linear hierarchy, and traits lower in the hierarchy as closer to the class/object which is the subject of the linearisation. The linearisation affects how `super´ is resolved in a trait: it will resolve to the closest base trait (higher in the hierarchy).
  */
class StringWriterTest extends FunSuite {

  test("testWrite 1") {
    val w = new BasicStringWriter
      with UppercasingStringWriter
      with CapitalizingStringWriter
    println(w.write("hello world"))
  }

  test("testWrite 2") {
    val w = new BasicStringWriter
      with CapitalizingStringWriter
      with LowercasingStringWriter
    println(w.write("heLLO WoRld"))
  }

  // TODO: add more tests

}
