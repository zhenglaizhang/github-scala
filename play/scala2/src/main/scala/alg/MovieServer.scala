package net.zhenglai
package alg

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import java.nio.file.Paths
import scala.io.StdIn

object MovieServer {
  val route: Route = path("download") {
    {
      complete(HttpEntity.fromPath(
        ContentTypes.`text/csv(UTF-8)`,
        Paths.get("play/scala2/src/main/scala/alg/movies.csv"),
        chunkSize = 5120))
    }
  }

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("MovieServer")
    implicit val ec = system.dispatcher
    val binding = Http()
      .newServerAt("localhost", 3000)
      .bind(route)
    StdIn.readLine()
    binding
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
