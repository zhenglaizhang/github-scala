package net.zhenglai
package net.zhenglai.util

import org.scalatest.FunSuite

class JsonPrintableTest extends FunSuite {
  import JsonPrintableInstances._
  import JsonPrintableSyntax._

  final case class A(a: Int, b: String)
  final case class B(a: Seq[A], b: Boolean)

  test("[anyVal].json") {
    val a = 12
    assert(a.json == "12")
  }

  test("[anyRef].json") {

    val a = B(Seq(A(11, "11a"), A(22, "22a")), b = false)
    println(a.json)
//    assert(a.json == """{"a":[{"a":11,"b":"11a"},{"a":22,"b":"22a"}],"b":false}
//                       |""".stripMargin)
  }

}
