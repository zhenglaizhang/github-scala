package net.zhenglai.github
package context

import scala.concurrent.ExecutionContext

object Implicits {
  class GitHubContext(val host: String, ec: ExecutionContext) {}
  implicit final val Global =
    new GitHubContext(host = "https://api.github.com", ExecutionContext.global)
}
