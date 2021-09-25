// type classes are “type system constructs that support ad hoc polymorphism”

trait Summable[A] {
  def sumElements(xs: List[A]): A
}

object Summable {
  // If you try this, you will notice that it works for Strings and Ints, and it doesn’t even compile for anything else:
  implicit object IntSummable extends Summable[Int] {
    def sumElements(xs: List[Int]): Int = xs.sum
  }

  implicit object StringSummable extends Summable[String] {
    def sumElements(xs: List[String]): String = xs.mkString
  }
}

object typeclass {

  def processMyListOld[T](list: List[T]): T = {
    // aggregate a list
    // int => sum
    // string => concatentation
    ???
  }

  // In Scala, we can enhance this method with implicit arguments which can enhance its capability
  // and constrain its use at the same time
  // the implicit works as both a capability enhancer and a type constraint, because if the compiler cannot find an implicit instance of a ListAggregation of that particular type, i.e. your specialized implementation, then it’s certain that the code can’t run.
  def processMyList[A](xs: List[A])(implicit summable: Summable[A]): A =
    summable.sumElements(xs)
}

@main def tc(): Unit = {
  import typeclass.*
  processMyList(List(1, 2, 3))
  processMyList(List("a", "b"))
}

// The behavior we’ve just implemented is called “ad hoc polymorphism” because the sumElements ability is unlocked only in the presence of an implicit instance of the trait which provides the method definition, right there when it’s called, hence the “ad hoc” name. “Polymorphism” because the implementations we can provide can obviously be different for different types, as we did with Int and String.