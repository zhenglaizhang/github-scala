package net.zhenglai
package collection

trait Codec[A] { self =>
  def encode(value: A): String
  def decode(value: String): A // doesn't account for failures yet
  def imap[B](dec: A => B, enc: B => A): Codec[B] =
    new Codec[B] {
      override def encode(value: B): String = self.encode(enc(value))
      override def decode(value: String): B = dec(self.decode(value))
    }
}

object Codec {
  def encode[A](value: A)(implicit c: Codec[A]): String = c.encode(value)
  def decode[A](value: String)(implicit c: Codec[A]): A = c.decode(value)
}

object CodecInstances {
  implicit val stringCodec: Codec[String] = new Codec[String] {
    override def encode(value: String): String = value
    override def decode(value: String): String = value
  }

  implicit val intCodec: Codec[Int] = stringCodec.imap(_.toInt, _.toString)
  implicit val booleanCodec: Codec[Boolean] =
    stringCodec.imap(_.toBoolean, _.toString)

//  implicit val intCodec: Codec[Int] = new Codec[Int] {
//    override def encode(value: Int): String = value.toString
//    override def decode(value: String): Int = value.toInt
//  }

}
