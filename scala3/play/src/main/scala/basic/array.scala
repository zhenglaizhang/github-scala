package net.zhenglai
package basic

@main def array() = {
  val a = Array(1, 2, 3)
  // update method is a point where the compiler is intervening and implementing
  // the method by bridging to a facility of the platform
  a.update(1, 12)
  a(2) = 11
  a.foreach(println)
}
