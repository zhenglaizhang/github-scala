package net.zhenglai.github
package model

import scala.concurrent.Future

trait GitHubAPI[Req, Resp] extends (Req => Future[Resp]) {
  def headers: Map[String, String]
}
