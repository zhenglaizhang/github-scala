package net.zhenglai
package util

import util.Math.squareRoot

import org.scalatest.FunSuite

class MathTest extends FunSuite {

  test("testSquareRoot") {
    squareRoot.isDefinedAt(-1)
    assert(squareRoot(625) === 25)
    assert(squareRoot.lift(625) === Some(25))
    assert(List(-1, 9, 4, 0, -3).collect(squareRoot) === Seq(3, 2, 0))

    val square: PartialFunction[Int, Double] = {
      case a if a < 0 ⇒ java.lang.Math.pow(a, 2)
    }
    val f = squareRoot.orElse(square)
    assert(f.isDefinedAt(-1) === true)
  }

}
