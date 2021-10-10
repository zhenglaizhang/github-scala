package net.zhenglai
package play.cat

import cats.data.Reader

final case class Cat(name: String)

// Readers
//  - configuration injection as parameters
//  - write program steps ad instances of Reader, chain together with map & flatMap
//    build a function that accepts the dependency as input
// TODO: cake pattern & DI frameworks
object ReaderTest {
  // map extends the computation in the Reader
  // by passing its result through a function
  def map(): Unit = {
    val x: Reader[Cat, String] = Reader(cat => cat.name)
    val y: Reader[Cat, String] = x.map(_.reverse)
    println(y.run(Cat("name")))
  }

  // combine readers that depend on same input
  def flatmap(): Unit = {
    val feed: Reader[Cat, String] = Reader(cat => s"feeding ${cat.name}")
    val greet: Reader[Cat, String] = Reader(cat => s"greet ${cat.name}")
    val greetAndFeed = for {
      g <- greet
      f <- feed
    } yield s"$g, $f"
    val r = greetAndFeed(Cat("wow"))
    println(r)
  }

  def main(args: Array[String]): Unit = {
    flatmap()
  }
}
