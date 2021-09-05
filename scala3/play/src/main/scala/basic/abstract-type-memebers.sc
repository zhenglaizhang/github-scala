import scala.io.Source

// type member works best when it evolves in parallel with the enclosing type
// family polymorphism or covariant specialization

abstract class BulkReader:
  // abstract type member
  type In
  // abstract field
  val source: In
  // abstract method
  def read: Seq[String]

case class StringBulkReader(source: String) extends BulkReader:
    // all concrete type members are aliases for other types
    type In = String
    override def read: Seq[String] = Seq(source)

case class FileBulkReader(source: Source) extends BulkReader:
    type In = Source // alias to Source
    override def read : Seq[String] = source.getLines.toVector


type Rec = (String, Double)
def transform(rec: Rec): Rec = ???