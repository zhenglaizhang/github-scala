package net.zhenglai
package types

import org.scalatest.FunSuite

class packageTest extends FunSuite {

  test("testStringExtension.isAllUpperCase") {
    assert(!"test".isAllUpperCase)
    assert("TEST".isAllUpperCase)
  }

  test("testIterableExtension.saveToDatabase") {
    Seq(1, 2, 3).saveToDatabase()
  }

}
