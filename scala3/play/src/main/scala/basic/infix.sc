import scala.annotation.targetName

@targetName("Tie") // annotate symbolic operator definitions to Java
case class <+>[A, B](a: A, b: B)
// val ab1: Int <+> String = 1 <+> "one"
val ab2: Int <+> String = <+>(1, "One")

// need infix as the name is alphanumeric
infix case class tie[A, B](a: A, b: B)
// val ab3: Int tie String = 1 tie "one"
val ab4: Int tie String = tie(1, "one")


// targetName workaround type erasure
object O:
    @targetName("m_seq_int")
    def m(xs: Seq[Int]): Int = xs.sum
    @targetName("m_seq_string")
    def m(xs: Seq[String]): Int = xs.length

O.m(List(1,2))
O.m(List("1", "w"))
