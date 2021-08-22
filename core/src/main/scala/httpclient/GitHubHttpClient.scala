package net.zhenglai.github
package httpclient

import akka.http.scaladsl.model.{HttpRequest, ResponseEntity}
import akka.http.scaladsl.unmarshalling.Unmarshaller

import scala.concurrent.Future

trait GitHubHttpClient {
  def newRequest[Req](
      uri: String,
      headers: Map[String, String] = Map.empty
  ): HttpRequest

  def run[Resp](req: HttpRequest)(implicit
      um: Unmarshaller[ResponseEntity, Resp]
  ): Future[Resp]
}
