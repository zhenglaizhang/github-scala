package net.zhenglai
package github

package object model {
  type GitHubCall[Req, Resp] = (Req, Resp)
  type GitHubErrorOr[A] = Either[GitHubError, A]
  type GitHubResp[A] = GitHubErrorOr[A]
}
