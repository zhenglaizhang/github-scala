package net.zhenglai
package types

import org.scalatest.FunSuite

class StringWriterTest extends FunSuite {

  test("testWrite 1") {
    val w = new BasicStringWriter
      with UppercasingStringWriter
      with CapitalizingStringWriter
    println(w.write("hello world"))
  }

  test("testWrite 2") {
    val w = new BasicStringWriter
      with LowercasingStringWriter
      with CapitalizingStringWriter
    println(w.write("hello world"))
  }

  // TODO: add more tests

}
