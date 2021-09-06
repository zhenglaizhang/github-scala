package net.zhenglai
package types

import types.Stats.variance

import org.scalatest.FunSuiteLike

class StatsTest extends FunSuiteLike {

  test("testVariance") {
    println(variance(Vector(1, 2, 3, 4, 5)))
  }

}
