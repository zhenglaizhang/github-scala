package net.zhenglai.github.context

object Implicits {
  class GitHubContext(val host: String) {}
  implicit final val Global = new GitHubContext(host = "https://api.github.com")
}
