package net.zhenglai.scala3

enum Shape:
  case Circle, Rectangle


object Point:
  def apply(x: Int, y: Int) = ???


class Wow() {}


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
  def main(args : Array[String]) : Unit = {
    // Array in scala is mutable
    args(1) = "abc"
  }
  @main def hello() =
    println("hello scala3")
}
