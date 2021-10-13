package net.zhenglai
package net.yao.json

import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable

class CollectionTest extends AnyFunSuite {

  test("array") {
    //    val ints: Array[Int] = new Array[Int](5)
    val ints = new Array[Int](5)
    Array(1, 12, 2, 2)
    assert(ints.length == 5)
    assert(ints(0) == 0)

    ints.update(1, 1)
    println(ints.mkString(" "))

    //use array companion object
    // val doubles1 = Array[Double](1.0, 2.0)
    val doubles = Array(1.0, 2.0)
    assert(doubles(0) == 1.0)
    doubles(0) = 3.0
    assert(doubles(0) == 3.0)

    for (i <- 0 to doubles.length - 1)
      myPrint(doubles(i))
    println()

    for (i <- 0 until doubles.length)
      myPrint(doubles(i))
    println()

    for (i <- doubles.indices)
      myPrint(doubles(i))
    println()

    for (elem <- doubles)
      myPrint(elem)
    println()

    val iterator = doubles.iterator
    while (iterator.hasNext) {
      myPrint(iterator.next())
    }
    println()

    //    doubles.foreach((x: Double) => println(x))
    doubles.foreach(myPrint)
    println()

    println(doubles.mkString(" === "))

    //add
    //    doubles :+ 4.0
    val addTail = doubles.:+(4.0)
    println(addTail.mkString(" === "))
    assert(!(addTail sameElements doubles))

    //    5.0 +: doubles
    val addedHead = doubles.+:(5.0)
    println(addedHead.mkString(" === "))

  }

  test("mutable array") {
    //    val ints = new mutable.ArrayBuffer[Int]()
    val ints = mutable.ArrayBuffer(1, 2, 3, 3)
    println(ints)
    println(ints.mkString("*"))

    assert(ints(0) == 1)

    ints += 12
    println(ints)
    val addedInts = ints.addOne(1)
    println(ints)

    assert(addedInts eq ints)
    11 +=: ints
    println(ints)

    val array = ints.toArray
    println(array.mkString("*"))

    val buffer = array.toBuffer
    println(buffer.mkString("-"))

  }

  test("dimensional array") {
    val array = Array.ofDim[Int](2, 3)
    println(array.map(_.mkString(" ")).mkString("\n"))

    array.foreach(_.foreach(myPrint))

  }

  test("immutable list") {
    val list = List(1, 2, 3)
    println(list)
    list.foreach(myPrint)
  }

  test("word count 1") {
    val wordList = List("hello scala", "hello scala kafka",
      "hello scala kafka redis")
    val wordCount = wordList.flatMap(_.split(" "))
      .groupBy(identity)
      .map(kv => (kv._1, kv._2.size))
      .toList
      .sortWith(_._2 > _._2)
      .take(3)
    println(wordCount)
  }

  test("word count 2") {
    val wordCount = List(("hello scala", 1),
      ("hello scala kafka kafka", 2),
      ("hello scala kafka redis", 2))
      .flatMap(kv => {
        val words = kv._1.split(" ")
        words.map(word => (word, kv._2))
      })
      .groupBy(_._1)
      .map(kv => (kv._1, kv._2.map(_._2).sum))
      .toList
      .sortWith(_._2 > _._2)
      .take(3)
    println(wordCount)

    val wordCount2 = List(("hello scala", 1),
      ("hello scala kafka kafka", 2),
      ("hello scala kafka redis", 2))
      .flatMap(kv => {
        val words = kv._1.split(" ")
        words.map(word => (word, kv._2))
      })
      .groupBy(_._1)
      .view
      .mapValues(value => value.map(_._2).sum)
      .toList
      .sortWith(_._2 > _._2)
      .take(3)
    println(wordCount2)
  }

  def myPrint(x: Any): Unit = {
    print(x)
    print(" ")
  }

}
