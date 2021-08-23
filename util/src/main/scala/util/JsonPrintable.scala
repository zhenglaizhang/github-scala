package net.zhenglai
package util

import org.json4s.native.Serialization
import org.json4s.{Formats, NoTypeHints}

// like cats.Show, render an A instance as json for AnyRef or plain for AnyVal
sealed trait JsonPrintable[-A] {
  def jsonStr(a: A): String
}

object JsonPrintable {
  def jsonStr[A](a: A)(implicit jp: JsonPrintable[A]): String = jp.jsonStr(a)
}

object JsonPrintableInstances {
  implicit def anyValInstances[A <: AnyVal]: JsonPrintable[A] =
    new JsonPrintable[A] {
      override def jsonStr(a: A): String = a.toString
    }

  implicit def anyRefInstances[A <: AnyRef]: JsonPrintable[A] =
    new JsonPrintable[A] {
      implicit val formats: Formats =
        Serialization.formats(NoTypeHints)
      override def jsonStr(a: A): String = {
        Serialization.write(a)
      }
    }
}

object JsonPrintableSyntax {
  implicit class JsonPrintableOps[A](a: A) {
    def jsonStr(implicit jp: JsonPrintable[A]): String = jp.jsonStr(a)
  }
}
