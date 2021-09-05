package net.zhenglai
package logging

enum Level:
  case Info, Warn, Error
  def ==(that: Level): Boolean = this.ordinal == that.ordinal
  def >=(that: Level): Boolean = this.ordinal >= that.ordinal

trait Logging {
  import Level.*
  def level: Level
  def log(level: Level, message: String): Unit
  final def info(message: String): Unit =
    if level >= Info then log(Info, message)
  final def warn(message: String): Unit =
    if level >= Warn then log(Warn, message)
  final def error(message: String): Unit =
    if level >= Error then log(Error, message)
}

trait StdoutLogging extends Logging {
  import Level.*
  override def log(level : Level, message : String) : Unit = {
    val s = s"${level.toString.toUpperCase}: $message"
    level match {
      case Info | Warn => System.out.println(s)
      case Error => System.err.println(s)
    }
  }
}