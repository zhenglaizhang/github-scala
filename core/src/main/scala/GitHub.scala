package net.zhenglai.github

import context.Implicits.GitHubContext
import httpclient.AkkaHttpClient

import scala.concurrent.Future

case class GitHubRepo(
    id: Long,
    name: String,
    full_name: String, // TODO: snake case (underscore notation)
    owner: Owner
)

class RepoAPI(implicit ctx: GitHubContext) {
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import spray.json.DefaultJsonProtocol._

  import scala.concurrent.ExecutionContext.Implicits.global
  implicit val ownerFormat = jsonFormat2(Owner)
  implicit val gitHubRepoFormat = jsonFormat4(GitHubRepo)
  val http = new AkkaHttpClient()

  def get(owner: String, repo: String): Future[GitHubRepo] = {
    http.newRequest[GitHubRepo](uri = s"${ctx.host}/repos/$owner/$repo")
  }

  // TODO: paging
  def listRepos(
      org: String
  ): Future[List[GitHubRepo]] = {
    http.newRequest[List[GitHubRepo]](
      s"${ctx.host}/orgs/$org/repos"
    )
  }

}

object RepoAPI {
  def apply(implicit ctx: GitHubContext) = new RepoAPI
}

class GitHub {
  def repos(implicit ctx: GitHubContext) = new RepoAPI()
}

object GitHub {
  def apply(token: String): GitHub = new GitHub
}

case class Owner(login: String, id: Long)
