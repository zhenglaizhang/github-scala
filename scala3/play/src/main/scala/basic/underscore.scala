// Scala overloaded the meaning of the underscore in many, many places

object underscore {
  // 1. Ignoring
  val _ = 4
  val onlyFives = (1 to 10).map(_ => 5)
  val (a, _) = 12 -> 3

// we use self-types as a type restriction
  trait Singer
// trait Actor { _: Singer =>
  trait Actor { self: Singer =>
    def act(): Unit
  }

  // `_` is deprecated for wildcard arguments of types: use `?` instead
  def process(xs: List[Option[?]]): Int = xs.length

  // 2. Wildcards
  // import scala._
  // `_` is no longer supported for a wildcard import; use `*` instead
  12 match {
    case _ => "I am fine with everything"
  }

  // 3. default initializers
  // Particularly for variables, when we don’t know what to initialize them with, let the JVM decide - zero for numerical values, false for Booleans, null for reference type
  // `= _` has been deprecated; use `= uninitialized` instead.
  // var myvar: String = uninitialized

  // 4. lambda sugars
  List(1, 2).map(_ * 5)
  val sumF: (Int, Int) => Int = _ + _

  // 5. Eta-expansion
  // Underscores are also used to turn methods into function values
  // The underscore is a signal to the compiler to create a new lambda with the implementation of x => incrementer(x)
  def incr(x: Int) = x + 1
  val incrF = incr /*_*/
  // The syntax `<function> _` is no longer supported;

  // 6. Higher-Kinded Types
  // HKTs are generic types, whose type arguments are themselves generic
  class MyHignerKindedJewel[M[_]]
  val my = new MyHignerKindedJewel[List]

  // 7. Vararg methods
  def makeSentence(words: String*) = ???
  val words = List("a", "b")
  // The syntax `x: _*` is no longer supported for vararg splices; use `x*` instead
  val love = makeSentence(words *)
}
