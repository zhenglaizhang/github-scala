import scala.io.Source

abstract class BulkReader:
  // abstract type member
  type In
  // abstract field
  val source: In
  // abstract method
  def read: Seq[String]

case class StringBulkReader(source: String) extends BulkReader:
    type In = String
    override def read: Seq[String] = Seq(source)

case class FileBulkReader(source: Source) extends BulkReader:
    type In = Source // alias to Source
    override def read : Seq[String] = source.getLines.toVector