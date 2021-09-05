object scala2 {
  sealed trait Option[+A]
  final case class Some[+A](a: A) extends Option[A]
  case object None extends Option[Nothing]
}

// enums and sealed hierachy
enum Option[+A] {
  case Some(a: A)
  case None
}

// scala2 use _ as wirdcard, => for alias imports
// scala3 use * as wirdcard, as for alias imports
import java.util
import scala.math.*
import scala.collection.immutable.{List, Map}
import scala.collection.immutable.{BitSet as _, LazyList, HashMap as HMap}