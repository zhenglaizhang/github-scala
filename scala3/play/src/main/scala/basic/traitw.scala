package net.zhenglai
package basic

enum LoggingLevel:
  case Debug, Info, Warn, Error, Fatal

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