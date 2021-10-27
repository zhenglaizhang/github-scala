package net.zhenglai

import scala.annotation.tailrec

object p22 extends S99Data {
  def range(start: Int, end: Int): List[Int] = (start, end) match {
    case (i, j) if i <= j => i :: range(i + 1, j)
    case _ => Nil
  }

  def rangeTailRec(start: Int, end: Int): List[Int] = {
    @tailrec
    def loopR(i: Int, result: List[Int]): List[Int] = {
      if (i < start) result
      else loopR(i - 1, i +: result)
    }

    loopR(end, Nil)
  }

  def rangeBuiltin(start: Int, end: Int): List[Int] = List.range(start, end + 1)

  def main(args: Array[String]): Unit = {
    val r = range(4, 9)
    val r2 = rangeTailRec(4, 9)
    println(r)
    assert(r == List(4, 5, 6, 7, 8, 9))
    assert(r == r2)
    assert(r == rangeBuiltin(4, 9))
  }
}
