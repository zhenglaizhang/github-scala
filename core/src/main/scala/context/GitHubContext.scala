package net.zhenglai.github
package context

import httpclient.{AkkaHttpClient, GitHubHttpClient}

import scala.concurrent.ExecutionContext

object Implicits {
  class GitHubContext(
      val host: String,
      val ec: ExecutionContext,
      val hc: GitHubHttpClient
  )
  implicit final val Global =
    new GitHubContext(
      host = "https://api.github.com",
      ec = ExecutionContext.global,
      hc = new AkkaHttpClient()
    )

//  class RunContext(
//      val ec: ExecutionContext
//  )
}
