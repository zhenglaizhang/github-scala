package net.zhenglai
package types

import types.control.{continue, manage}

import org.scalatest.FunSuite

import scala.io.Source

class controlTest extends FunSuite {

  test("test manage") {
    println(System.getProperty("user.dir"))
    val lines = manage(Source.fromFile("build.sbt")) { source =>
      source.getLines().toSeq
    }
    assert(lines.nonEmpty)
  }

  test("test continue") {
    var count = 0
    continue(count < 5) {
      println(s"at $count")
      count += 1
    }
    assert(count == 5)
  }

}
