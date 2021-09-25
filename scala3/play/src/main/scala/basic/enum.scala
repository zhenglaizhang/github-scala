// You should now be able to safely define and use enums, add parameters, methods and fields 
// and use their pre-defined APIs
object scala2 {
  sealed trait Option[+A]
  final case class Some[+A](a: A) extends Option[A]
  case object None extends Option[Nothing]
}

object scala2way {
  // object Permissions extends Enumeration {
  //   val READ, WRITE, EXEC, NONE = Value
  // }
}

enum Permissions {
  case READ, WRITE, EXEC
  // Under the hood, the compiler generates a sealed class and 3 well-defined values in its companion object
}

// enums and sealed hierachy
// enum syntax provides same benefits as sealed type hierachies, but with less code
enum Option[+A] {
  case Some(a: A)
  case None
}

// scala2 use _ as wirdcard, => for alias imports
// scala3 use * as wirdcard, as for alias imports
import java.util
import scala.collection.immutable.{LazyList, List, Map, HashMap as HMap, BitSet as _}
import scala.math.*


// ADT
// enums can also have arguments
// Enums can contain fields and methods, just like a normal class - they’re compiled to a sealed class after all
// A nice addition to enums is the ability to create companion objects, where we can define “static” fields and methods, perhaps “smart” constructors
enum WeekDay(val fullName: String):
  case Sun extends WeekDay("Sunday")
  case Mon extends WeekDay("Monday")
  case Tue extends WeekDay("Tuesday")
  case Wed extends WeekDay("Wednesday")
  case Thu extends WeekDay("Thursday")
  case Fri extends WeekDay("Friday")
  case Sat extends WeekDay("Saturday")
  def isWorkingDay: Boolean = !(this == Sat || this == Sun)

object WeekDay {
  def fromStr(name: String): WeekDay = ???
}


// Enums come with some predefined utility methods
@main def enumtest1(): Unit = {
  import WeekDay.*
  val sorted = WeekDay.values.sortBy(_.ordinal).toSeq
  WeekDay.values   // Array[WeekDay] java mutable and no good equals
  println(Sun.ordinal)
  // Sun.fullName
  Sun.isWorkingDay
  WeekDay.valueOf("Sun")
  // WeekDay.valueOf("notexists")
  // java.lang.IllegalArgumentException: enum case not found: notexists

}



enum Tree[A]:
  // extending Tree is inferred by the compiler
  case Branch(left: Tree[A], right: Tree[A])
  case Leaf(elem: A)

@main def enumtest2(): Unit = {
  import Tree.*
  val tree = Branch(
    Branch(
      Leaf(1),
      Leaf(2)
    ),
    Branch(
      Leaf(3),
      Branch(
        Leaf(3),
        Branch(Leaf(4), Leaf(5))
      )
    )
  )
}
