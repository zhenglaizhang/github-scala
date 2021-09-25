import java.io.IOException

// quick decomposition of data

val countingList = List(1, 2, 3, 4)
// list extractors
countingList match {
  case List(_, _, 3, somethingElse) => s"someThingElse: $somethingElse"
}

// haskell-like prepending
List(1, 2, 3, 4) match {
  case 1 :: someOtherElements => println(someOtherElements)
}

def processList(xs: List[Int]): String = xs match {
  case Nil => ""
  case h :: t => h + " " + processList(t)
}

processList(List(1, 2, 3, 4))

// List vararg pattern
List(1, 2, 3, 4) match {
  case List(_, 2, _*) => "only care 2nd element as 2"
  // The * is the important bit, which means “any number of additional arguments”. This pattern is much more
  // flexible because an (almost) infinite number of lists can match this pattern
}

// List infix patterns
// The :+ is the append operator,
// which works much like :: from the point of view of pattern matching.
// You can also use the +: prepend operator (for symmetry),
// but we prefer ::.
// A nice benefit of the append operator is we can combine it
// with the vararg pattern for a really powerful structure
List(1, 2, 3, 4) match {
  case List(1, _*) :+ 42 => "found meaning of life"
}

// type specifier
def givemeAValue(): Any = 123
givemeAValue() match {
  case _: String => "string"
  case i: Int if i < 0 => "negative int" // conditional guards
  case i: Int if i >= 0 => "natural int"
  case _ => "something else"
}

try {
  throw new Exception("boom")
} catch {
  case _: IOException => "IO failed"
  case _: Exception | _: RuntimeException => "we could have prevented that"
}

// name binding
case class Person1(name: String, age: Int, favoriteMovies: List[String])

// conditional guards
def anumber() = 1
anumber() match {
  case 1 => "first"
  case 2 => "second"
  case 3 => "third"
  case n if n % 10 == 1 => n + "st"
  case n if n % 10 == 2 => n + "nd"
  case n if n % 10 == 3 => n + "rd"
  case n => n + "th"
}

// Alternative patternsPermalink
// The only drawback of this pattern is that you can’t bind any names, because the compiler can’t ensure those values
// are available on the right-hand side.
List(1, 2, 3) match {
  case List(1, _, _) | List(43, _*) => "i like the list"
  case _ => "I dont like the list"
}

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

// name binding
dean match {
  case p @ Person(name, _, movies @ Address(_, _, country)) => s"$name's info ${country}'"
}

def stats(): (Int, Double, Double, Double, Double) = (1, 1.1, 1,1,1.4)
val (count, sum, avg, min, max) = stats()

val str = """Book: "Programming Scala", by Dean Wampler"""
val s"""Book: "$title", by $author""" = str: @unchecked
assert(title == "Programming Scala")
assert(author == "Dean Wampler")

// Pattern bindings will throw MatchError when match fails
// add @unchecked if know the declaration is safe and match is exhaustive
val h4a +: h4b +: t4 = Seq(1, 2, 3, 4) : @unchecked

// MatchError at runtime
// val h4a +: h4b +: t4 = Seq(1 ) : @unchecked

// In for comprehension, matching that isn't exhaustive functions as a FILTER
val xs = Seq((1,2), "hello", ("one", "two"))
for
  case (x, y) <- xs
yield (x, y)

for
  case Some(x) <- Seq(Some(1), None, Some(2))
yield x

// Pattern matching and erasure
def seqMatch[A](xs: Seq[A]): String = xs match
  case Nil => "unknown"
  case head +: _ => head match
    case _: Double => "Double"
    case _: String => "String"
    case _ => "Others"

seqMatch(Seq((1,2.1), Nil, Seq("a", "b"), Seq(1,2)))