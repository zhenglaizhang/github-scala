package net.zhenglai
package collection

import org.scalatest.FunSuite

class GCounterTest extends FunSuite {

  test("testMerge") {
    import InitiaVersion._
    val gc1 = new GCounter[Int]()
      .increment("m1", 1)
      .increment("m2", 2)
    val gc2 = new GCounter[Int]()
      .increment("m1", 3)
    val gc = gc1.merge(gc2)
    println(gc)
    println(gc.total)
  }

}
