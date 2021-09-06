package net.zhenglai
package cool

import org.scalatest.{FunSuite, FunSuiteLike}

class UtilTest extends AnyFunSuite {
  import Util.*
  test("testSeqToString") {
    val xs = Seq(1, 2, 3, 4, 5.4)
    println(seqToString(xs))
  }

  test("testReverseSeqToString") {
    val xs = Seq(1, 2, 3, 4, 5.4)
    println(reverseSeqToString(xs))
  }
}
