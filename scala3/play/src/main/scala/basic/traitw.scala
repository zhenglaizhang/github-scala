package net.zhenglai
package basic

// The first problem is that sometimes in large code bases (and not only), extending the same trait multiple times is not unheard of. What happens if you mix-in a trait with one argument in one place, and with another argument in another place
// The short answer is that won’t compile. The rule is: if a superclass already passes an argument to the trait, if we mix it again, we must not pass any argument to that trait again

trait Talker(subject: String) {
  def talkWith(another: Talker): String
}

abstract class Perso(name: String) extends Talker("rock")
abstract class RockFan extends Talker("rock")
class RockFanatic extends RockFan with Talker {// must not pass argument here
  override def talkWith(another: Talker): String = ???
}

// What happens if we define a trait hierarchy? How should we pass arguments to derived traits
// Derived traits will not pass arguments to parent traits
trait BrokenRecord extends Talker

// compile fails because BrokenRecord doesn't take arguments
// class AnnoyingFriend extends BrokenRecord("politics")
abstract class AnnonyingFriend extends BrokenRecord with Talker("politics")
// Only way to make the type system sound with respect to this new capability of traits

enum LoggingLevel:
  case Debug, Info, Warn, Error, Fatal

// Trait Arguments
trait Logger(val level: LoggingLevel):
  def log(msg: String): Unit

trait ConsoleLogger extends Logger:
  override def log(msg : String) : Unit = println(s"${level.toString.toUpperCase}: $msg")

class Service(val name: String, level: LoggingLevel) extends ConsoleLogger with Logger(level):
  def run(): Unit = log("hello")

@main def t() = {
  val s = Service("abc", LoggingLevel.Debug)
  s.run()
}


trait T11:
  val denom: Int // overhead
  lazy val inverse = 1.0 / denom

trait T12:
  val denom: Int
  def inverse = 1.0 / denom


trait T13(val denom: Int):
  val inverse = 1.0 / denom

@main def traitparam() = {
  val obj1 = new T11:
    val denom = 10

  val obj2 = new T13(10) {}
}

// Transparent Traits

// Assume further that the trait Paintable is rarely used as a standalone trait, but rather as an auxiliary trait in our library definitions
// We can suppress Paintable from type inference by marking it with super
transparent trait Paintable
trait Color 
case object Red extends Color with Paintable
case object Green extends Color with Paintable
case object Blue extends Color with Paintable
@main def wow1(): Unit = {
  val color: Color & Product & Serializable = if (43 > 2) Red else Blue
  // The complete inferred type is Color with Product with Serializable
  // They are case objects, they automatically implement the traits Product (from Scala) and Serializable (from Java). So the lowest common ancestor is the combination of all three
}

// So Scala 3 allows us to ignore these kinds of traits in type inference, by making them a super trait
// When Scala 3 comes out, the traits Product, Comparable (from Java) and Serializable (from Java) will be automatically be treated as super traits in the Scala compiler. Of course, if you mark your value as having a particular type, super traits will not influence the type checker