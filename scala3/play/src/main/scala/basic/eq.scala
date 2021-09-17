package net.zhenglai
package basic


object bad {
  case class X(name: String)
  case class Y(name: String)


  def findMarkers[A](xs: Seq[A]): Seq[A] =  xs.filter(_ == X("marker"))

  @main def testbad() = {
    val x = findMarkers(Seq(X("one"), X("two"), X("marker")))
    val y = findMarkers(Seq(Y("one"), Y("two"), Y("marker")))
    println(x)
    println(y) // List()
  }
}

object good {
  import scala.language.strictEquality
  case class X(name: String) derives CanEqual
  case class Y(name: String) derives CanEqual

  def findMarkers[A](marker: A, xs: Seq[A])(using CanEqual[A, A]): Seq[A] = xs.filter(_ == marker)

  @main def testgood() = {
    val x = findMarkers(X("marker"), Seq(X("one"), X("two"), X("marker")))
    val y = findMarkers(Y("marker"), Seq(Y("one"), Y("two"), Y("marker"))) // Nil
    println(x)
    println(y)
  }
}


class ABC()

// eq tests for reference quality, only defined for AnyRef
@main def eq() = {
  // the result of an implicit conversion must be more specific than Object
  // println(1 eq 2)
  println(ABC() eq ABC())
  println(ABC() ne ABC())

  // == is a method delegates to equals
  println(ABC() == ABC())


  // Arrays are defined by Java, equals does reference comparision, like eq
  val tru = Array(1, 2) sameElements Array(1, 2)
  println(tru)
}

case object O1
case object O2:
  val f = "O2"
  def m(i: Int): String = i.toString

object O3:
  case object O4

// The compiler-generated hashCode for a case object simply hashes the object’s name, without considering its
// members or its nesting inside other objects or packages. O3 behaves better, but it’s not a case object.
// Avoid using case objects as keys in maps and sets or other contexts where hashCode is used.
@main def caseobjecthashcode() = {
  println(O1.hashCode() == "O1".hashCode)
  println(O2.hashCode() == "O2".hashCode)
  println(O3.hashCode() != "O3".hashCode) // O3 is not case object
  println(O3.O4.hashCode() == "O4".hashCode)
  println(O3.O4.hashCode() != "O3".hashCode())
}