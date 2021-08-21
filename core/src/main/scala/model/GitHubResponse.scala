package net.zhenglai.github
package model

// TODO: use own definitions
import akka.http.scaladsl.model.StatusCode

trait GitHubResponse[Resp] {
  def statusCode: StatusCode
  def resp: Either[Resp, GitHubError]
}
