package net.zhenglai
package types

import java.util.StringTokenizer

class SentenceParserTokenize {
  def parse(s: String): Array[String] = {
    val tokenizer = new StringTokenizer(s)
    Iterator
      .continually({
        val hasMore = tokenizer.hasMoreTokens
        if (hasMore) {
          (hasMore, tokenizer.nextToken())
        } else {
          (hasMore, null)
        }
      })
      .takeWhile(_._1)
      .map(_._2)
      .toArray
  }
}

class SentenceParserSplit {
  def parse(s: String): Array[String] =
    s.split("\\s")
}
