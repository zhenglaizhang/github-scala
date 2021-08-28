package net.zhenglai
package util

import util.Cool.{FunChainable, concatString, parallelFoldMap}

import cats.Id
import org.scalatest.FunSuite
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime

import scala.concurrent.{Await, Future}

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

  test("parallelFoldMap") {
    val x: Future[String] =
      parallelFoldMap(('a' to 'z').map(_.toString))(x => x + x + x + "\n")
    val y = Await.result(x, 1.second)
    println(y)
  }

}
