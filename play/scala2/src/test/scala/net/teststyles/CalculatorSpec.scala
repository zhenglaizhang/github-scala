package net.zhenglai
package net.teststyles

import org.scalatest.funspec.AnyFunSpec

// Structured, Nested, Described Tests: FunSpecs
// This style of testing is focused on testing specifications, i.e. behaviors. It still doesn’t use the full
// expressiveness of Scala, but now we have more powerful testing structures:
//  - we can nest tests into one another
//  - we can add pinpoint descriptions to particular tests
// In ScalaTest, FunSpecs are used for structured, descriptive tests. The style is still similar to the above
// FunSuites, but we are now moving towards testing behaviors (BDD), which is more powerful and easy to reason about
// in large codebases.
class CalculatorSpec extends AnyFunSpec {
  val calculator = new Calculator

  // can nest as many levels deep as you like
  describe("multiplication") {
    it("should give back 0 if multiplying by 0") {
      assert(calculator.multiply(572389, 0) == 0)
      assert(calculator.multiply(-572389, 0) == 0)
      assert(calculator.multiply(0, 0) == 0)
    }
  }
  describe("division") {
    it("should throw a math error if dividing by 0") {
      assertThrows[ArithmeticException](calculator.divide(57238, 0))
    }
  }
}
