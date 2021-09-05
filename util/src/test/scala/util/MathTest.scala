package net.zhenglai
package util

import util.Math.squareRoot

import org.scalatest.FunSuite

import java.util.Scanner

class MathTest extends FunSuite {

  test("testSquareRoot") {
    squareRoot.isDefinedAt(-1)
    assert(squareRoot(625) === 25)
    assert(squareRoot.lift(625) === Some(25))
    assert(List(-1, 9, 4, 0, -3).collect(squareRoot) === Seq(3, 2, 0))

    val square: PartialFunction[Int, Double] = {
      case a if a < 0 => java.lang.Math.pow(a, 2)
    }
    val f = squareRoot.orElse(square)
    assert(f.isDefinedAt(-1) === true)
  }

  test("Mean.calc") {
    import net.zhenglai.util.Math.Mean._
    assert(calc(1.2, 1.3, 1.2, 1.3) == 1.25)
    assert(calc(Seq(1.2, 1.0, 1.2, 1.0)) == 1.1)
    // calc(Nil) // NaN
  }

  test("wow, match identifier from java") {
    val input = "1 fish 2 fish red fish blue fish"
    val s = new Scanner(input).useDelimiter("\\s*fish\\s*")
    try {
      // surround with single back quotes (backticks)
      s.`match`()
    } catch {
      case _: IllegalStateException => println("not matched")
    }
    assert(s.nextInt == 1)
    assert(s.nextInt == 2)
    assert(s.next == "red")
    assert(s.next == "blue")
  }

}
