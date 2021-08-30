package net.zhenglai
package types

import org.scalatest.FunSuite

import scala.language.reflectiveCalls

class SentenceParserSplitTest extends FunSuite {

  def printSentenceParts(
      sentence: String,
      parser: { // better to use trait
        // duck typing affects code quality and uses reflection, so very slow
        def parse(sentence: String): Array[String]
      }
  ): Unit = parser.parse(sentence).foreach(println)

  test("testParse") {
    val tokenizeParser = new SentenceParserTokenize
    val splitParser = new SentenceParserSplit
    val s = "hello world Scala is great"
    printSentenceParts(s, tokenizeParser)
    printSentenceParts(s, splitParser)
  }

}
