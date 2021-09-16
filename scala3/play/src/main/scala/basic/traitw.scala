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