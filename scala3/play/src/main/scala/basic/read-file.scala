import scala.io.Source
import java.io.File

@main def source1(): Unit = {
  // returns an Iterator of the lines in the file
  // The best part is that this iterator is not fully loaded in memory, so unlike version 2, you can read the file slowly rather than load everything in memory and then disposing of the contents.
  val contentIterator = Source.fromFile("/tmp/test").getLines()
}

def open(path: String): File = new File(path)

implicit class RichFile(file: File) {
  def read() = Source.fromFile(file).getLines()
}

@main def pythonway(): Unit = {
  val readLikeBoss = open("/tmp/test").read()
}
