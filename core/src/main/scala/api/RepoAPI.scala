package net.zhenglai.github
package api

import context.Implicits.GitHubContext
import model.{GitHubAPI, Headers}

import scala.concurrent.Future

class RepoAPI(implicit gc: GitHubContext) {
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import spray.json.DefaultJsonProtocol._
  implicit val ownerFormat = jsonFormat2(Owner)
  implicit val gitHubRepoFormat = jsonFormat4(GitHubRepo)

  final case class GetParam(owner: String, repo: String)
  final case class ListReposParam(owner: String)

  object get extends GitHubAPI[GetParam, GitHubRepo] {

    def apply(getParam: GetParam): Future[GitHubRepo] = {
      val req = gc.hc.newRequest(uri =
        s"${gc.host}/repos/${getParam.owner}/${getParam.repo}"
      )
      gc.hc.run[GitHubRepo](req)
    }

    def apply(owner: String, repo: String): Future[GitHubRepo] =
      apply(GetParam(owner, repo))

    override def headers: Map[String, String] =
      Map("Accept" -> Headers.Accept.VndGitHubV3Json)
  }

  object listRepos extends GitHubAPI[ListReposParam, List[GitHubRepo]] {
    def apply(listReposParam: ListReposParam): Future[List[GitHubRepo]] = {
      val req = gc.hc.newRequest(
        s"${gc.host}/orgs/${listReposParam.owner}/repos"
      )
      gc.hc.run[List[GitHubRepo]](req)
    }

    def apply(org: String): Future[List[GitHubRepo]] =
      apply(ListReposParam(org))

    override def headers: Map[String, String] =
      Map("Accept" -> Headers.Accept.VndGitHubV3Json)
  }
}

object RepoAPITest {}
