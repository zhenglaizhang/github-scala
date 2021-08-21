package net.zhenglai.github
package httpclient

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{
  HttpRequest,
  HttpResponse,
  ResponseEntity,
  StatusCodes
}
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}

import scala.concurrent.{ExecutionContext, Future}

class AkkaHttpClient {
  implicit val system = ActorSystem(Behaviors.empty, "github-api-akka-system")

  def newRequest[Resp](
      uri: String
  )(implicit
      ec: ExecutionContext,
      um: Unmarshaller[ResponseEntity, Resp]
  ): Future[Resp] = {
    val r: Future[HttpResponse] = Http().singleRequest(
      HttpRequest(uri = uri)
    )
    r.flatMap {
      case HttpResponse(StatusCodes.OK, _, entity, _) ⇒
        Unmarshal(entity).to[Resp]
      case HttpResponse(status, _, e, _) ⇒
        e.discardBytes()
        Future.failed(new RuntimeException(""))
    }

  }
}
