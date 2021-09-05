package net.zhenglai
package types

import org.scalatest.FunSuite

import scala.io.Source

class manageTest extends FunSuite {

  test("testApply") {
    println(System.getProperty("user.dir"))
    val lines = manage(Source.fromFile("build.sbt")) { source =>
      source.getLines().toSeq
    }
    assert(lines.nonEmpty)
  }

}
