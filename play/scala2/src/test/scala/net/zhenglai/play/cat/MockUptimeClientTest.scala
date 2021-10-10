package net.zhenglai
package play.cat

import cats.Id
import org.scalatest.FunSuite

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

class MockUptimeClientTest extends FunSuite {

  test("testGetTotalUptime Sync") {
    val testUptimeClient = new MockUptimeClient(Map("a" -> 12, "b" -> 13))
    val uptimeService = new UptimeService[Id](testUptimeClient)
    val totalUptime: Id[Int] = uptimeService.getTotalUptime(List("a", "d"))
    assert(totalUptime === 12)
  }

  test("testGetTotalUptime Async") {
    val testUptimeClient = new AsyncUptimeClient()
    val uptimeService = new UptimeService[Future](testUptimeClient)
    val totalUptime: Future[Int] = uptimeService.getTotalUptime(List("a", "b"))
    assert(Await.result(totalUptime, 2.seconds) > 0)
  }

}
