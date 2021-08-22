package net.zhenglai.github
package api

import context.Implicits.GitHubContext
import model.{GitHubAPI, GitHubResp, Headers}

import spray.json.RootJsonFormat

import scala.concurrent.Future

class RepoAPI(implicit gc: GitHubContext) {
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import net.zhenglai.github.model.GitHubErrorJsonFormat._
  implicit val ownerFormat: RootJsonFormat[Owner] = jsonFormat2(Owner)
  implicit val gitHubRepoFormat: RootJsonFormat[GitHubRepo] = jsonFormat4(
    GitHubRepo
  )

  final case class GetParam(owner: String, repo: String)
  final case class ListReposParam(owner: String)

  object get extends GitHubAPI[GetParam, GitHubRepo] {

    def apply(getParam: GetParam): Future[GitHubResp[GitHubRepo]] = {
      val req = gc.hc.newRequest(uri =
        s"${gc.host}/repos/${getParam.owner}/${getParam.repo}"
      )
      gc.hc.run[GitHubRepo](req)
    }

    def apply(owner: String, repo: String): Future[GitHubResp[GitHubRepo]] =
      apply(GetParam(owner, repo))

    override def headers: Map[String, String] =
      Map("Accept" -> Headers.Accept.VndGitHubV3Json)
  }

  object listRepos extends GitHubAPI[ListReposParam, List[GitHubRepo]] {
    def apply(
        listReposParam: ListReposParam
    ): Future[GitHubResp[List[GitHubRepo]]] = {
      val req = gc.hc.newRequest(
        s"${gc.host}/orgs/${listReposParam.owner}/repos"
      )
      gc.hc.run[List[GitHubRepo]](req)
    }

    def apply(org: String): Future[GitHubResp[List[GitHubRepo]]] =
      apply(ListReposParam(org))

    override def headers: Map[String, String] =
      Map("Accept" -> Headers.Accept.VndGitHubV3Json)
  }
}

object RepoAPITest {}
