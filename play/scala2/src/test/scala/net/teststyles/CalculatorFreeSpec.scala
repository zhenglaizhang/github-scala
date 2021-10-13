package net.zhenglai
package net.teststyles

import org.scalatest.freespec.AnyFreeSpec

// This style of testing is very similar to word specs, in the sense that the tests can be read like natural language
// . Besides that, the testing structure is now free of constraints (hence the Free name prefix): we can nest them,
// we can create predefined test preparations, we can nest on different levels, etc. This test is still in the
// behavior (BDD) world, hence the Spec name

class CalculatorFreeSpec extends AnyFreeSpec {
  val calculator = new Calculator
  // The - operator takes the significance of any previous keyword in the word-spec style. It can mean anything,
  // which makes this ScalaTest testing style very powerful and flexible
  "A calculator" - { // anything you want
    "give back 0 if multiplying by 0" in {
      assert(calculator.multiply(653278, 0) == 0)
      assert(calculator.multiply(-653278, 0) == 0)
      assert(calculator.multiply(0, 0) == 0)
    }

    "throw a math error if dividing by 0" in {
      assertThrows[ArithmeticException](calculator.divide(653278, 0))
    }
  }
}
