package net.zhenglai
package net.teststyles

import org.scalatest.funsuite.AnyFunSuite

// FunSuites
// This style of testing is straightforward. Each test comes with its own description and its own body. This is the
// style of JUnit, MUnit or other unit testing frameworks that use this same structure.
// In ScalaTest, FunSuites are used for JUnit-like, independent tests.
class CalculatorSuite extends AnyFunSuite {
  val calculator = new Calculator
  test("multiplication with 0 should always give 0") {
    assert(calculator.multiply(124141, 0) == 0)
    assert(calculator.multiply(-123131, 0) == 0)
    assert(calculator.multiply(0, 0) == 0)
  }

  test("dividing by 0 should throw a math error") {
    assertThrows[ArithmeticException](calculator.divide(1231, 0))
  }
}
