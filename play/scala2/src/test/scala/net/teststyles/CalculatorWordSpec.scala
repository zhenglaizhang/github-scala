package net.zhenglai
package net.teststyles

import org.scalatest.wordspec.AnyWordSpec

// Structured, Nested, Expressive Testing: WordSpecs
// Still in the Spec world (BDD), this style of testing takes more advantage of the expressiveness of the Scala
// language. The tests look more like natural language through the use of “keyword”-methods e.g. should, must
//  - The beauty of this style of testing is that we can still nest tests inside one another, so that in the end we get
//    a massive suite of tests that perfectly describe what our code-under-test is supposed to do and when.
//  - Expressiveness aside, this testing style enforces a relatively strict testing structure, thereby aligning all
//    team members to the same testing convention.
class CalculatorWordSpec extends AnyWordSpec {
  val calculator = new Calculator
  "A calculator" should {
    "give back 0 if multipling by 0" in {
      assert(calculator.multiply(653278, 0) == 0)
      assert(calculator.multiply(-653278, 0) == 0)
      assert(calculator.multiply(0, 0) == 0)
    }
    "throw a math error if dividing by 0" in {
      assertThrows[ArithmeticException](calculator.divide(12313, 0))
    }
  }
}
