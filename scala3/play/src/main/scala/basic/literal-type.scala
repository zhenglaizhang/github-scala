// Scala is now able to treat literal values as singletons of their own types
object literal {
  def pass(n: 3) = println(n)
  def passi(n: Int) = println(n)
  val num: Int = 3 // an Int
  // If you don’t specify the type of your val, the compiler will infer the type based on the right-hand side as it did before. If you specify the literal type yourself, the compiler will automatically build a new type behind the scenes, based on what you declared. Any literal can become its own type, and it will be a subtype of its “normal” type, e.g. the type 3 will be a subtype of Int
  val three: 3 = 3
  pass(3)
  pass(three)

  // String is a reference type,
  // String interning happens automatically for String literals
  val scalas: "Scala" = "Scala"
  val pi: 3.14 = 3.14
  val truth: true = true

  // Literal types are used to enforce compile-time checks
  // to your definitions so that you don’t go reckless with your code
  def meow(meaning: scala.Option[42]): Unit =
    meaning.foreach(x => "I am exactly 42")
  // only 2 possible call parameters:
  meow(Some(42))
  meow(None)
  // It’s often a good idea to restrict your type declarations to literal types
  // when you’re certain that your particular value is critical for your application’s logic.
  // In this way, you’ll avoid the error of passing an invalid value, and everything will be caught at compile time
}
