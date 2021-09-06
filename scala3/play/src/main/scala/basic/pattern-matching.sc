def seqToString[A](xs: Seq[A]): String = xs match
  case head +: tail => s"($head +: ${seqToString(tail)})"
  case Nil => "Nil"

seqToString(List(1, "two", 3, 4.4))


val langs = Seq(
  ("Scala",   "Martin", "Odersky"),
  ("Clojure", "Rich",   "Hickey"),
  ("Lisp",    "John",   "McCarthy"))



val l2 : Seq [Tuple] = EmptyTuple +: ("A" *: EmptyTuple) +: langs

l2 map {
  case "Scala" *: first *: last *: EmptyTuple => s"Scala -> $first -> $last"
  case lang *: rest => s"$lang -> $rest"
  case EmptyTuple => EmptyTuple.toString
}


val tuples = Seq((1,2,3))
tuples map {
  case (x, y, z) => x + y + z
}

// parameter untupling
tuples map {
  (x, y, z) => x + y + z
}

tuples map (_+_+_)
