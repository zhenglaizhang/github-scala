def seqToString[A](xs: Seq[A]): String = xs match
  case head +: tail => s"($head +: ${seqToString(tail)})"
  case Nil => "Nil"

seqToString(List(1, "two", 3, 4.4))