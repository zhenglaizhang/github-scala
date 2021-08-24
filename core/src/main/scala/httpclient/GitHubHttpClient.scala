package net.zhenglai.github
package httpclient

import akka.http.scaladsl.model.{HttpRequest, ResponseEntity}
import akka.http.scaladsl.unmarshalling.Unmarshaller
import net.zhenglai.github.model.GitHubResp

import scala.concurrent.Future

trait GitHubHttpClient {

  def newRequest[Req](
      uri: String,
      headers: Map[String, String] = Map.empty
  ): HttpRequest

  def run[Resp](req: HttpRequest)(implicit
      um: Unmarshaller[ResponseEntity, GitHubResp[Resp]]
  ): Future[GitHubResp[Resp]]
}
