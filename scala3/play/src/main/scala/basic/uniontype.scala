package net.zhenglai
package basic

def process(i: Int): String | Int | Double =
  if (i < 0) then "Negative"
  else if (i < 10) then i.toDouble
  else i

@main def ut() = {
  val s: String | Double | Int = process(12)
  s match {
    case s: String => println(s)
    case i: Int => println(i)
    case d: Double => println("null")
  }
}