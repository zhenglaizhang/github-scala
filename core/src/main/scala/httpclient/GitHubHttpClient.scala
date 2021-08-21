package net.zhenglai.github
package httpclient

import model.{GitHubRequest, GitHubResponse}

import scala.concurrent.Future

trait GitHubHttpClient {
  def request[Req, Resp](
      req: GitHubRequest[Req]
  ): Future[GitHubResponse[Resp]]
}
