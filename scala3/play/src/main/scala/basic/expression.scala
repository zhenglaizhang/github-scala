import java.lang.Thread

trait Action {
  def act(x: Int): Int
}

object single_abstract_method {
  // the single abstract method pattern
  // Since Scala 2.12, abstract classes or traits with a single unimplemented method can be reduced to lambdas.
  val ma: Action = (x: Int) => x + 1
  // compilers automatically convert the lambda into an anonymous instance of Action
  val ma2: Action = new Action {
    override def act(x: Int): Int = x + 1
  }

  // no need to create Runnable anonymous instance
  new Thread(() => println("I ran easily!")).start()
}

// Methods with non-alphanumeric names which end in a colon, like ::, are right-associative
class MessageQueue[A] {
  def -->:(value: A): MessageQueue[A] = ???
}
object right_associative_method {
  val x1 = 1 :: 2 :: 3 :: List()
  val x2 = List().::(3).::(2).::(1)
  assert(x1 == x2)
  val queue = 3 -->: 2 -->: 1 -->: new MessageQueue[Int]
}

// Trick 3 - baked-in “setters”
class MutableIntWrapper {
  private var _intVal = 0
  def intVal = _intVal
  def intVal_=(newVal: Int): Unit = _intVal = newVal
}

// multi-word members
class Person(name: String) {
  def `then said`(thing: String): String = s"$name then said: $thing"
}

@main def test_exp(): Unit = {
  val w = new MutableIntWrapper
  w.intVal = 12
  w.intVal

  val zzl = new Person("zzl")
  zzl `then said` "Scala is cool!"
}
// another example is akka http content type, e.g.
// ContentTypes.`application/x-www-form-urlencoded

// backtick pattern matching
// the ability to match an existing variable exactly. 
object backtick_pattern_matching {
  val x = 12
  val v = 12
  v match {
    case v => "shadowning v"
  }
  v match {
    case v1 if v1 == v => "workaround"
  }
  v match {
    case `v` => "match exactly v"
  }
}

