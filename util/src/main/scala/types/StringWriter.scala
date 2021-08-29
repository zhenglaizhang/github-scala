package net.zhenglai
package types

abstract class StringWriter {
  def write(data: String): String
}

class BasicStringWriter extends StringWriter {
  override def write(data: String): String = data
}

trait CapitalizingStringWriter extends StringWriter {
  abstract override def write(data: String): String = {
    super.write(data.split("\\s+").map(_.capitalize).mkString(" "))
  }
}

trait UppercasingStringWriter extends StringWriter {
  abstract override def write(data: String): String =
    super.write(data.toUpperCase)
}

trait LowercasingStringWriter extends StringWriter {
  abstract override def write(data: String): String =
    super.write(data.toLowerCase)
}
