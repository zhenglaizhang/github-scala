package net.zhenglai
package types

import types.Stats.variance

import org.scalatest.FunSuite

class StatsTest extends FunSuite {

  test("testVariance") {
    println(variance(Vector(1, 2, 3, 4, 5)))
  }

}
