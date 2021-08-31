package net.zhenglai.scala3

enum Message:
  case Draw(shape: Shape)
  case Response(message: String)
  case Exit

enum Shape:
  case Circle, Rectangle


object Point:
  def apply(x: Int, y: Int) = ???


class Wow() {}

@main def rangeTest() : Unit = {
  10 to 1 by -3
  1L to 10L by 3
  ('a' to 'g' by 3).foreach(println) //
}


@main def outmostMain(params: String*): Unit = params.foreach(println)

@main def withArgs(a: String, b: Int, c: Boolean) = {
  import Shape.* // new wirdcard, replaces `_`
  println(Circle)
  println(Wow()) // no `new`
  println(a)
  println(b)
  println(c)
}

object HelloScala3 {

  def enumMatch = {
    import Message.*
    val m: Message = ???
    m match {
      case Draw(s) => println(s)
      case Response(m) => println(m)
      case Exit => println(Exit)
    }
  }

  def main(args : Array[String]) : Unit = {
    // Array in scala is mutable
    args(1) = "abc"
  }
  @main def hello() =
    println("hello scala3")
}
