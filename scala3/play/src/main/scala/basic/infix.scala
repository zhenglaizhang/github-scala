import scala.annotation.targetName

// Scala is expressive and powerful in many ways, but two (quite simple) features make it distinctive from most other languages:
//  - method naming including non-alphanumeric characters, which allow math-like operators such as ++, -->, ? and more
//  - the ability to infix methods with a single argument
//    - This works for any method with one argument and allows us to write object.method(arg) or object method arg

// Infix Methods and Scala 3, Now Explicit
//  - infix notation for “regular” (i.e. alphanumeric) methods will be discouraged and deprecated (according to the docs starting in Scala 3.1). Expressions like mary likes "Forrest Gump" will now compile with a warning. However, Scala 3 allows us to remove the warning if the method comes annotated with infix keyword
//  - The infix keyword is only required for alphanumeric method names

object wi {
  @targetName("Tie") // annotate symbolic operator definitions to Java
  case class <+>[A, B](a: A, b: B)
  // val ab1: Int <+> String = 1 <+> "one"
  val ab2: Int <+> String = <+>(1, "One")
}

// need infix as the name is alphanumeric
infix case class tie[A, B](a: A, b: B)
// val ab3: Int tie String = 1 tie "one"
val ab4: Int tie String = tie(1, "one")


case class Meow(name: String)

// targetName workaround type erasure
object O:
    @targetName("m_seq_int")
    infix def m(xs: Seq[Int]): Int = xs.sum
    @targetName("m_seq_string")
    def m(xs: Seq[String]): Int = xs.length

    // extension method
    extension (m: Meow)
      infix def likes(movie: String): String = s"${m.name} likes $movie"


@main def infix_main(): Unit = {
    import O.*
    O.m(List(1,2))
    O.m(List("1", "w"))
    O m List(1, 2)
    val me = Meow("wow")
    me likes "cool"
}
