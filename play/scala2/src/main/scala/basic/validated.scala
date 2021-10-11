package net.zhenglai
package basic

import cats.kernel.Semigroup

object validated {
  // Validated is therefore more powerful than Either, because besides giving us the freedom to pick the types for
  // the “errors”, it allows us to
  //  - combine multiple errors into one instance, thus creating a comprehensive report
  //  - process both values and errors, separately or at the same time
  //  - convert to/from Either, Try and Option

  import cats.data.Validated

  val v: Validated[String, Int] = Validated.valid(42)
  val iv: Validated[String, Int] = Validated.invalid("something bad")
  val test: Validated[String, Int] = Validated.cond(42 > 39, 99, "too small bad")

  // Validated shines where error accumulation is required.

  def validatePositive(n: Int): Validated[List[String], Int] =
    Validated.cond(n > 0, n, List("Number must be positive"))

  def validateSmall(n: Int): Validated[List[String], Int] =
    Validated.cond(n < 100, n, List("Number must be smaller than 100"))

  def validateEven(n: Int): Validated[List[String], Int] =
    Validated.cond(n % 2 == 0, n, List("Number must be even"))

  import cats.instances.list._

  implicit val combineIntMax: Semigroup[Int] = Semigroup.instance[Int](Math.max)

  def validate(n: Int): Validated[List[String], Int] =
    validatePositive(n)
      .combine(validateSmall(n))
      .combine(validateEven(n))

  def main(args: Array[String]): Unit = {
    println(validate(100))
    println(validate(98))
    println(validate(-11))
    //Invalid(List(Number must be smaller than 100))
    //Valid(98)
    //Invalid(List(Number must be positive, Number must be even))
  }
}
