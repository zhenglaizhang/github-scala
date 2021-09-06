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

// match on regular expressions
val BookExtractorRE = """Book: title=([^,]+),\s+author=(.+)""".r
val MagazineExtractorRE = """Magazine: title=([^,]+),\s+issue=(.+)""".r
val catalog = Seq(
  "Book: title=Programming Scala Third Edition, author=Dean Wampler",
  "Magazine: title=The New Yorker, issue=January 2021",
  "Unknown: text=Who put this here??"
)
catalog map {
  case BookExtractorRE(title, author) => s"""Book "$title" written by $author"""
  case MagazineExtractorRE(title, author) => s"""Magine "$title" written by $author """
  case entry => s"Unrecognized entry: $entry"
} foreach println

// matching on interpolated strings
catalog map {
  case s"""Book: title=$t, author=$a""" => ("Book" -> (t -> a))
  case s"""Magazine: title=$t, issue=$i""" => ("Magazine" -> (t -> i))
  case item => ("Unrecognized", item)
} foreach println

// pattern bindings
case class Address(street: String, city: String, country: String)
case class Person(name: String, age: Int, address: Address)
val addr = Address("1 Scala Way", "CA", "USA")
val dean = Person("Dean", 29, addr)

val Person(name, age, Address(_, state, _)) = dean
val nas =
  for
    Person(name, age, Address( _, state, _)) <- Some(dean)
  yield (name, age, state)


def stats(): (Int, Double, Double, Double, Double) = (1, 1.1, 1,2,1,3,1.4)
val (count, sum, avg, min, max) = stats()

val str = """Book: "Programming Scala", by Dean Wampler"""
val s"""Book: "$title", by $author""" = str: @unchecked
assert(title == "Programming Scala")
assert(author == "Dean Wampler")

// Pattern bindings will throw MatchError when match fails
// add @unchecked if know the declaration is safe and match is exhaustive
val h4a +: h4b +: t4 = Seq(1, 2, 3, 4) : @unchecked

// MatchError
// val h4a +: h4b +: t4 = Seq(1 ) : @unchecked
