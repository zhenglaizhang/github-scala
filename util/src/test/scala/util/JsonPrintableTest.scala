package net.zhenglai
package util

import org.scalatest.FunSuite
final case class A1(a: Int, b: String)
final case class B(a: Seq[A1], b: Boolean)

class JsonPrintableTest extends FunSuite {
  import JsonPrintableInstances._
  import JsonPrintableSyntax._

  test("[anyVal].json") {
    val a = 12
    assert(a.jsonStr === "12")
  }

  test("[anyRef].json") {

    val a = B(Seq(A1(11, "11a"), A1(22, "22a")), b = false)
    assert(
      a.jsonStr === """{"a":[{"a":11,"b":"11a"},{"a":22,"b":"22a"}],"b":false}"""
    )
  }

}
