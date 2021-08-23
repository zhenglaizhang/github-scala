package net.zhenglai
package net.zhenglai.util

import org.json4s.native.Serialization
import org.json4s.{Formats, NoTypeHints}

// like cats.Show, render an A instance as json for AnyRef or plain for AnyVal
sealed trait JsonPrintable[A] {
  def json(a: A): String
}

object JsonPrintable {
  def json[A](a: A)(implicit jp: JsonPrintable[A]): String = jp.json(a)
}

object JsonPrintableInstances {
  implicit def anyValInstances[A <: AnyVal]: JsonPrintable[A] =
    new JsonPrintable[A] {
      override def json(a: A): String = a.toString
    }

  implicit def anyRefInstances[A <: AnyRef]: JsonPrintable[A] =
    new JsonPrintable[A] {
      implicit val formats: Formats =
        Serialization.formats(NoTypeHints)
      override def json(a: A): String = {
        Serialization.write(a)
      }
    }
}

object JsonPrintableSyntax {
  implicit class JsonPrintableOps[A](a: A) {
    def json(implicit jp: JsonPrintable[A]): String = jp.json(a)
  }
}
