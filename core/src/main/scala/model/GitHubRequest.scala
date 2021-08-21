package net.zhenglai.github
package model

trait GitHubRequest[Req] {
  def headers: Map[String, String]
  def param: Req
}
