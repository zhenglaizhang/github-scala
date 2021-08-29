package net.zhenglai
package util

import util.Math.squareRoot

import org.scalatest.FunSuite

class MathTest extends FunSuite {

  test("testSquareRoot") {
    squareRoot.isDefinedAt(-1)
    squareRoot(100)
    squareRoot.lift(-1)
    println(List(-1, 2, 3, 4, 0, -3).collect(squareRoot))

    val square: PartialFunction[Int, Double] = {
      case a if a < 0 ⇒ java.lang.Math.pow(a, 2)
    }
    val f = squareRoot.orElse(square)
    println(f.isDefinedAt(-1))
  }

}
