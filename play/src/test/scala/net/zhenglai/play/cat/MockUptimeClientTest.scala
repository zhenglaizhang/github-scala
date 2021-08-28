package net.zhenglai
package play.cat

import cats.Id
import org.scalatest.FunSuite

class MockUptimeClientTest extends FunSuite {

  test("testGetUpTime") {
    val testUptimeClient = new MockUptimeClient(Map("a" -> 12, "b" -> 13))
    val uptimeService = new UptimeService[Id](testUptimeClient)
    val totalUptime = uptimeService.getTotalUptime(List("a", "d"))
    assert(totalUptime === 12)
  }

}
