package net.zhenglai
package play.col

// By default, Scala uses an immutable set.
// It doesn’t maintain any order for storing elements.
object set {
  def main(args: Array[String]): Unit = {
    val s1 = Set()
    val s2 = Set(1, 2, 3, 2, 1)
    val mutableS = scala.collection.mutable.Set(1, 2, 3)
    println(mutableS)
    println(s2.head)
    println(s2.tail)
    assert(s1.isEmpty)
  }
}