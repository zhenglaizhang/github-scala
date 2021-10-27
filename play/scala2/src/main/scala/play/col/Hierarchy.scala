package net.zhenglai
package play.col

class Hierarchy {
  // Scala’s collection classes begin with the Traversable and Iterable traits. These traits branch into three main
  // categories: List, Set, and Seq.
  //
  // The Traversable trait allows us to traverse an entire collection. It’s a base trait for all other collections. It
  // implements the common behavior in terms of a foreach method.
  //
  // The Iterable trait is the next trait from the top of the hierarchy and a base trait for iterable collections. It
  // defines an iterator which allows us to loop through a collection’s elements one at a time. When we use an
  // iterator, we can traverse the collection only once. This is mainly because each element gets processed during
  // the iteration process.
  // scala.Traversable
  // scala.Iterable
}

