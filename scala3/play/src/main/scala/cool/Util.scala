package net.zhenglai
package cool

object Util {
  def seqToString[A](xs: Seq[A]): String =
    xs match {
      case head +: tail => s"$head +: ${seqToString(tail)}"
      case Nil          => "Nil"
    }

  def reverseSeqToString[A](xs: Seq[A]): String =
    xs match {
      case head +: tail => s"${seqToString(tail)} :+ $head"
      case Nil          => "Nil"
    }
}
