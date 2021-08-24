package net.zhenglai
package util

import util.Cool.FunChainable

import org.scalatest.FunSuite

class CoolTest extends FunSuite {

  test("testFunChainable") {
    def f1(a: Int): String = a.toString
    def f2(a: String): Option[Char] = a.headOption
    def f3(a: Option[Char]): Option[Int] = a.map(_.toInt)
    val r = 12 |> f1 |> f2 |> f3
    assert(r == Option(49))
  }

}
