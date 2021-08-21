package net.zhenglai.github
package model

import scala.concurrent.Future

trait GitHubAPI[Req, Resp]
    extends (GitHubRequest[Req] => Future[GitHubResponse[Resp]]) {}
