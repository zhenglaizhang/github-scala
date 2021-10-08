package net.zhenglai
package basic

case class User(name: String, email: String)

object refined {
  import eu.timepit.refined.api.Refined
  import eu.timepit.refined.auto._
  import eu.timepit.refined.numeric._
  // Refined is a type that takes two type arguments: the “value” type you’re testing/validating, and the predicate you want the value to pass
  // The predicate is embedded as a type. Obviously, there’s some implicit conversion happening behind the scenes so you can simply write 42 on the right-hand side
  val positiveInt: Refined[Int, Positive] = 12

  // Use Refined in infix notation
  val negative: Int Refined Negative = -100
  val nonNegative: Int Refined NonNegative = 0
  val odd: Int Refined Odd = 3
  val even: Int Refined Even = 68

  import eu.timepit.refined.W
  val smallEnough: Int Refined Less[W.`100`.T] = 45
  // The W value is an alias for shapeless’ Witness, which is able to generate a type for us with the construct W.100.T

  // Refined Tools, Supercharged
  // The most useful validations happen on strings. Since so much of our application logic is dependent on strings
  import eu.timepit.refined.string._
  val commandPrompt: String Refined EndsWith[W.`"$"`.T] = "zhenglai@mbp $"
  val isRegex: String Refined Regex = "rege(x(es)?|xps?)"
  type Email =
    String Refined MatchesRegex[W.`"""[a-z0-9]+@[a-z0-9]+\\.[a-z0-9]{2,}"""`.T]
  type SimpleName = String Refined MatchesRegex[W.`"""[A-Z][a-z]+"""`.T]
  case class ProperUser(name: SimpleName, email: Email)
  val zzl = ProperUser("Zhenglai", "zhenglai@email.com")
  // val bad = ProperUser("zhenglai@email.com", "Zhenglai")
  // Predicate failed: "zhenglai@email.com".matches("[A-Z][a-z]+").

  // Refining at Runtime
  import eu.timepit.refined.api.RefType
  val poorEmail = "zhenglai"
  val refineCheck: Either[String, Email] = RefType.applyRef[Email](poorEmail)
}

object RefinedType {
  val zhenglai = User("Zhenglai", "zhenglai@email.com")

  // It would be perfectly fine to the compiler… until the application crashes at some point because you’re
  // attempting to send an email to “Daniel”.
  val bad = User("zhenglai@email.com", "Zhenglai")

  // We can leverage the power of the Scala compiler to validate certain predicates before our application has the
  // chance to run and ruin our day
  def main(args: Array[String]): Unit = {}
}
