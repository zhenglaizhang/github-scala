package net.zhenglai

import scala.reflect.ClassTag
import scala.util.Random

object p25 extends S99Data {
  def randomPermute[A](xs: List[A]): List[A] = {
    p23.rangeSelect(xs.length, xs)
  }

  // ClassTag
  // You don’t need a ClassTag for list, because it is a normal class / trait, with a generic parameter. When you
  // have a List[String] and a List[Int], type erasure will cause them to have the same class at runtime. All type
  // checks are done at compile time. In Java, you are allowed to leave generic parameters off, getting an unchecked
  // “raw type”, which is what you’ll have at runtime.
  // Arrays on the other hand have different runtime classes on the JVM. Array types are not erased, so an
  // Array[String] and and Array[Int] are different classes at runtime. For that reason, you need to know the type at
  // runtime to instantiate the class. This is what the ClassTag does, it holds the type at runtime.

  def randomPermuteCanocial[A: ClassTag](xs: List[A]): List[A] = {
    val rand = new Random()
    val a = Array.from(xs)
    for (i <- a.length - 1 to 1 by -1) {
      val ix = rand.nextInt(i + 1)
      val t = a(ix)
      a.update(ix, a(i))
      a.update(i, t)
    }
    a.toList
  }

  def main(args: Array[String]): Unit = {
    val r = randomPermute(charList)
    println(r)
    assert(charList.length == r.length)
    println()
    val r2 = randomPermuteCanocial(charList)
    println(r2)
  }
}
