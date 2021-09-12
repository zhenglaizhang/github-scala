package net.zhenglai
package logging
//import org.scalatest.FunSuite

import scala.util.Random

open class Service(name: String):
  def work(i: Int): (Int, Int) = (i, Random.between(0, 1000))

case class LoggedService(name: String, level: Level) extends Service(name) with ConsoleLogging {
  override def work(i : Int) : (Int, Int)  =
    info(s"Starting work: i = $i")
    val result = super.work ( i )
    info(s"Ending work: result = $result")
    result
}

@main def test() = {
  val svc = LoggedService("two", Level.Info)
  (1 to 3).foreach {i =>
    println(s"Result: ${svc.work(i)}")
  }
}

//class LoggingTest extends FunSuite {
//
//  test("loggingService") {
//    val svc = LoggedService("two", Level.Info)
//    (1 to 3).foreach {i =>
//      println(s"Result: ${svc.work(i)}")
//    }
//  }
//}
