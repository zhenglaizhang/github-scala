import java.io.File
import scala.io.Source
import scala.util.control.NonFatal

System.getProperty("user.dir")
new File("./").getCanonicalPath

def readFiles(fileNames: String*): Unit = {
  fileNames.foreach { fileName =>
    var source: Option[Source] = None
    try
      source = Some(Source.fromFile(fileName))
    catch
      // non fatal exceptions, e.g. reasonable to attempt recovery
      // harmless throwables
      case NonFatal(ex) => println(s"Non fatal exception: $ex")
    finally
      for s <- source do
        println(s"Closing $fileName")
        s.close()
  }
}

readFiles("build.sbt", "notexist.sc", "readme.md")


case class LineLoader(file : java.io.File) {

  import scala.compiletime.uninitialized

  // scala2 uses _ for unitialized var fields
  // scala3 uses uninitialized to make intention more clear
  // `uninitialized` can only be used as the right hand side of a mutable field definition
  private var source : Source = uninitialized
  val lines = try
    source = Source.fromFile ( file )
    source.getLines.toSeq
  finally
    if source != null then source.close ()
}