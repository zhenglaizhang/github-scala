package net.zhenglai
package net.teststyles

import org.scalatest.refspec.RefSpec

// RefSpecs are a BDD-style testing structure where the testing suite is described in plain language as an object
// whose name is written with backticks
// The object A calculator is a test suite, where each method is a test case. When we run this test, ScalaTest will
// inspect the RefSpec via reflection and turn the objects into test suites and methods into independent tests.
class CalculatorRefSpec extends RefSpec {
  // backtics isolate the name of this identifier. The same goes for class names and for method names.
  val `rock the jvm` = 42

  object `A calculator` {
    val calculator = new Calculator

    def `multiply by 0 should be 0`(): Unit = {
      assert(calculator.multiply(653278, 0) == 0)
      assert(calculator.multiply(-653278, 0) == 0)
      assert(calculator.multiply(0, 0) == 0)
      () // for discarded non-Unit value warning
    }

    def `should throw a math error when dividing by 0`(): Unit = {
      assertThrows[ArithmeticException](calculator.divide(653278, 0))
      ()
    }
  }
}
