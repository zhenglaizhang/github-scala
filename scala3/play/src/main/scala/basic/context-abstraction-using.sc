import scala.annotation.implicitNotFound

trait Tagify[A]:
  def toTag(a: A): String

case class Stringer[A: Tagify](a: A):
    override def toString: String = s"Stringer: ${summon[Tagify[A]].toTag(a)}"

object O:
  def makeXML[A](a: A)(using @implicitNotFound(s"makeXML: No Tagify[${A}] implicit found") tagger: Tagify[A]): String =
    s"<xml>${tagger.toTag(a)}</xml>"


given Tagify[Int]:
  def toTag(i: Int): String = s"<int>$i</int>"

given Tagify[String]:
  def toTag(s: String): String = s"<string>$s</string>"


Stringer("Hello World")
Stringer(100)
O.makeXML("Hello World")
O.makeXML(100)


Stringer(3.141)
O.makeXML(3.141)
