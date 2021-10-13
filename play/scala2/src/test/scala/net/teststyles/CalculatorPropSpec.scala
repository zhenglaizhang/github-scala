package net.zhenglai
package net.teststyles

import org.scalatest.propspec.AnyPropSpec

// ScalaTest is able to structure behavior (BDD) tests in the style of properties.
class CalculatorPropSpec extends AnyPropSpec {
  val calculator = new Calculator
  val multiplyByZeroExamples = List((653278, 0), (-653278, 0), (0, 0))

  // that means a comprehensive test that fully covers a property of the subject-under-test.
  property("Calculator multiply by 0 should be 0") {
    assert(multiplyByZeroExamples.forall {
      case (a, b) => calculator.multiply(a, b) == 0
    })
  }

  property("Calculator divide by 0 should throw some match error") {
    assertThrows[ArithmeticException](calculator.divide(1231, 0))
  }
}
