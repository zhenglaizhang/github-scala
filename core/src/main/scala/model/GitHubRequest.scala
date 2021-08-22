package net.zhenglai.github
package model

sealed trait GitHubRequest[Req] {
  def headers: Map[String, String]
  def param: Req
}

abstract class V3PreviewRequest[Req] extends GitHubRequest[Req] {
  val headers = Map("Accept" -> Headers.Accept.VndGitHubV3Json)
}
