object ScalaSearch:
  def unapply(s: String): Boolean = s.toLowerCase.contains("scala")

val books = Seq(
  "Programming Scala",
  "JavaScript: The Good parts",
  "Scala Cookbook"
).zipWithIndex

val results = for s <- books yield s match
  case (ScalaSearch(), index) => s"$index: Found Scala"
  case (_, index) => s"$index: No Scala"

