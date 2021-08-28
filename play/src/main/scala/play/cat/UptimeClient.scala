package net.zhenglai
package play.cat

import cats.{Applicative, Id}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

trait UptimeClient[F[_]] {
  def getUpTime(hostname: String): F[Int]
}

trait RealUptimeClient extends UptimeClient[Future] {
  override def getUpTime(hostname: String): Future[Int]
}

trait TestUptimeClient extends UptimeClient[Id] {
  override def getUpTime(hostname: String): Int
}

class AsyncUptimeClient(implicit
    ec: ExecutionContext
) extends RealUptimeClient {
  override def getUpTime(hostname: String): Future[Int] =
    Future(Random.nextInt(1000))
}

class MockUptimeClient(hostnames: Map[String, Int]) extends TestUptimeClient {
  override def getUpTime(hostname: String): Int =
    hostnames.getOrElse(hostname, 0)
}

import cats.syntax.functor._
import cats.syntax.traverse._
class UptimeService[F[_]: Applicative](uptimeClient: UptimeClient[F]) {
  def getTotalUptime(hostnames: List[String]): F[Int] = {
    hostnames.traverse(uptimeClient.getUpTime).map(_.sum)
  }
}
