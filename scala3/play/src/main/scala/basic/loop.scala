object loopisbad {
  var y = 4
  while (y < 0) {
    println("I am now in a loop")
    y += 1
  }
}

object make_fp_not_war {
  // In Scala, we think in terms of expressions, not instructions:
  (5 until 42).foreach(_ => println("I am doing it right"))
  List(1, 2, 3).filter(_ % 2 == 1).map(_ + 1)
  List(1, 2, 3).flatMap(n => Seq.fill(n)("I like FP"))
  // Want a single value out of the entire list? Use fold, count, maxBy, find and a variety of other transformations. You see, every “loop” has an equivalent transformation.
  // Dont ask “how can I loop through this?”
  // Instead, ask “how can I transform this into what I want?”.
}

object for_comprehension {
  // for comprehension
  // not real for loop
  val pairs = for {
    x <- List(1, 2, 3)
    y <- List('a', 'b', 'c')
  } yield (x, y)
  val pairs2 = List(1, 2, 3).flatMap(x => List('a', 'b', 'c').map(y => (x, y)))
  assert(pairs == pairs2)
}
