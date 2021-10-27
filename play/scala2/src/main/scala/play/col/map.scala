package net.zhenglai
package play.col

// A Map is a collection of key/value pairs where keys are always unique. Scala provides mutable and immutable
// versions of it. By default, an immutable version of the map is imported

object map {
  def main(args: Array[String]): Unit = {
    val m = Map(1 -> "a", 2 -> "b")
    println(m.keys)
    println(m.values)
    println(m.isEmpty)
    println(m.get(1))
    println(m.getOrElse(2, 'c'))
    val x: String = m(2)
    println(x)
    // throw NoSuchElementException
    // m(3)
  }
}