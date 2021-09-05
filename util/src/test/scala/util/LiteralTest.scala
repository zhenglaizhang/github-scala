package net.zhenglai
package util

import org.scalatest.FunSuite

class LiteralTest extends FunSuite {
  test("boolean literals") {
    val (t, f) = (true, false)
    assert(t)
    assert(!f)
  }

  test("triple double quotes multi line string") {
    val welcome = s"""Welcome!
                       | Hello!
                       | * (Gratuitous Star character!!)
                       | This line has a margin indicator.
                       |   This line has some extra whitespace.""".stripMargin
    println(welcome)

    assert("<hello><world>".stripPrefix("<").stripSuffix(">") == "hello><world")
  }

  test("symbol literals") {
    // symbol are interned strings
    // 2 symbols with same character sequences will actually refer to same object in memory
    val s1 = "mysymbol"
    def s2 = "my" + "symbol"
    val sym1 = Symbol(s1)
    val sym2 = Symbol(s2)
    assert(s1 eq s2)
    assert(sym1 eq sym2)
  }
}
