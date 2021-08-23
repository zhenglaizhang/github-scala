package net.zhenglai.github
package httpclient

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import net.zhenglai.github.model.{GitHubError, GitHubResp}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

class AkkaHttpClient() extends GitHubHttpClient {
  implicit val system = ActorSystem(Behaviors.empty, "github-api-akka-system")
  // todo, use global ec
  implicit val ec = system.executionContext

  def run[Resp](req: HttpRequest)(implicit
      um: Unmarshaller[ResponseEntity, Resp],
      um2: Unmarshaller[ResponseEntity, GitHubError]
  ): Future[GitHubResp[Resp]] = {
    Http()
      .singleRequest(request = req)
      .flatMap {
        case HttpResponse(StatusCodes.OK, _, entity, _) =>
          Unmarshal(entity)
            .to[Resp]
            .map(Right(_): GitHubResp[Resp])
        case HttpResponse(status, headers, e, _) =>
          println(s"bad github response: $status")
          println(s"\tstatus: $status")
          println(s"\theaders: $headers")
          println(
            s"\tbody: ${Await.result(e.toStrict(1.second), 2.seconds).data.utf8String}"
          )
          Unmarshal(e)
            .to[GitHubError]
            .map(Left(_))
        case _ =>
          throw new RuntimeException("unknown response")
      }
  }

  def newRequest[Req](
      uri: String,
      headers: Map[String, String] = Map.empty
  ): HttpRequest = {
    val s: Seq[HttpHeader] = headers.map {
      case (k, v) => RawHeader(k, v)
    }.toSeq
    HttpRequest(uri = uri)
      .withHeaders(s)

  }
}
