package net.zhenglai
package basic

transparent trait Flag
// Flag wont be part of the inferred type
// other transparent trait scala.Product, java.lang.Serializable, and java.lang.Comparable

trait T1A
trait T2B
class B

class C1 extends B with T1A with T2B

// Scala3 new syntax
class C extends B, T1A, T2B

@main def intersectiontype() = {
  // with as a type operator has been deprecated; use & instead
  // val b : B with T1 with T2 = new B with T1 with T2
  val b2 : B & T1 & T2 = new B with T1 with T2
  // println ( b.getClass )
  println ( b2.getClass )
}