package net.zhenglai.github
package httpclient

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}

import scala.concurrent.Future

class AkkaHttpClient() extends GitHubHttpClient {
  implicit val system = ActorSystem(Behaviors.empty, "github-api-akka-system")
  // todo, use global ec
  implicit val ec = system.executionContext

  def run[Resp](req: HttpRequest)(implicit
      um: Unmarshaller[ResponseEntity, Resp]
  ): Future[Resp] = {
    Http()
      .singleRequest(request = req)
      .flatMap {
        case HttpResponse(StatusCodes.OK, _, entity, _) ⇒
          Unmarshal(entity).to[Resp]
        case HttpResponse(status, _, e, _) ⇒
          e.discardBytes()
          Future.failed(new RuntimeException(""))
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
