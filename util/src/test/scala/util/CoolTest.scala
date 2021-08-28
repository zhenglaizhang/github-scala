package net.zhenglai
package util

import util.Cool.{FunChainable, concatString}

import cats.Id
import org.scalatest.FunSuite

class CoolTest extends FunSuite {

  test("testFunChainable") {
    def f1(a: Int): String = a.toString
    def f2(a: String): Option[Char] = a.headOption
    def f3(a: Option[Char]): Option[Int] = a.map(_.toInt)
    val r = 12 |> f1 |> f2 |> f3
    assert(r == Option(49))
  }

  test("testConcatString") {
    assert(concatString(Option("abc"), Option("def")) === Some("abcdef"))
    assert(
      concatString(List("abc", "def"), List("123", "456")) === List(
        "abc123",
        "abc456",
        "def123",
        "def456"
      )
    )
    assert(concatString("123": Id[String], "abc": Id[String]) === "123abc")
  }

}
